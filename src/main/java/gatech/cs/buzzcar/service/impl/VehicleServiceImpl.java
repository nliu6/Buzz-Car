package gatech.cs.buzzcar.service.impl;

import gatech.cs.buzzcar.entity.dto.VehicleDto;
import gatech.cs.buzzcar.entity.param.PublicSearchParam;
import gatech.cs.buzzcar.entity.vo.VehicleVo;
import gatech.cs.buzzcar.service.VehicleService;
import gatech.cs.buzzcar.utils.UserUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class VehicleServiceImpl implements VehicleService {


    @Resource
    private JdbcTemplate jdbcTemplate;

    private static VehicleVo mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return getVehicleVo(resultSet);
    }

    private static VehicleVo getVehicleVo(ResultSet resultSet) throws SQLException {
        VehicleVo vo = new VehicleVo();
        vo.setVin(resultSet.getString("vin"));
        vo.setManufacturerName(resultSet.getString("manufacturer_name"));
        vo.setVehicleType(resultSet.getString("vehicle_type"));
        vo.setModelName(resultSet.getString("model_name"));
        vo.setModelYear(resultSet.getInt("model_year"));
        vo.setFuelType(resultSet.getString("fuel_type"));
        vo.setMileage(resultSet.getInt("mileage"));
        vo.setSalePrice(resultSet.getBigDecimal("sale_price"));
        vo.setColors(resultSet.getString("colors"));
        return vo;
    }

    @Override
    public List<VehicleVo> search(PublicSearchParam publicSearchParam) {

        StringBuilder querySql = new StringBuilder("select " +
                " v.vin, v.`manufacturer_name`, v.`vehicle_type`, v.`model_name`, v.`model_year`, " +
                " v.`fuel_type`, v.`mileage`,v.sale_price , v.colors " +
                " from  `vehicle` v where  v.vin in (select it.vin from `inventory_transaction` it where it.sale_status='saleable') ");

        List<Object> params = new ArrayList<>();

        if (StringUtils.isNotBlank(publicSearchParam.getVehicleType())) {
            querySql.append(" and v.vehicle_type=? ");
            params.add(publicSearchParam.getVehicleType());
        }
        if (StringUtils.isNotBlank(publicSearchParam.getFuelType())) {
            querySql.append(" and v.fuel_type=? ");
            params.add(publicSearchParam.getFuelType());
        }
        if (StringUtils.isNotBlank(publicSearchParam.getManufacturerName())) {
            querySql.append(" and v.manufacturer_name=? ");
            params.add(publicSearchParam.getManufacturerName());
        }

        if (Objects.nonNull(publicSearchParam.getModelYear())) {
            querySql.append(" and v.model_year=? ");
            params.add(publicSearchParam.getModelYear());
        }

        if (Objects.nonNull(publicSearchParam.getColor())) {
            querySql.append(" and v.colors like concat('%',?,'%') ");
            params.add(publicSearchParam.getColor());
        }
        // which searches the manufacturer, model year, model name and description

        if (StringUtils.isNotBlank(publicSearchParam.getKeyword())) {
            querySql.append(" and ( (v.manufacturer_name like concat('%',?,'%') ) OR (v.model_name like concat('%',?,'%')) OR (v.vehicle_description like concat('%',?,'%')) )");
            params.add(publicSearchParam.getKeyword());
            params.add(publicSearchParam.getKeyword());
            params.add(publicSearchParam.getKeyword());
        }

        querySql.append(" order by v.vin asc");


        return jdbcTemplate.query(querySql.toString(), (resultSet, rowNum) -> {
            return getVehicleVo(resultSet);
        }, params.toArray());
    }

    @Override
    public int queryVehicleAvailableQuantityForSale() {
        String cntSql = "select count(1) from `vehicle` v where  v.vin in (select it.vin from `inventory_transaction` it where it.sale_status='saleable') ";
        Integer cnt = jdbcTemplate.queryForObject(cntSql, Integer.class);
        return Objects.nonNull(cnt) ? cnt : 0;
    }


    @Override
    @Transactional
    public int saveVehicle(VehicleDto dto) {
        dto.setInventoryClerk(UserUtils.getUsername());
        int cnt = 0;
        Integer row = jdbcTemplate.queryForObject("select count(1) from vehicle where vin=?", Integer.class, dto.getVin());
        if(Objects.nonNull(row) && row > 0){
            cnt = -1;
        }else{
            String insertSql = "insert into vehicle(vin, `manufacturer_name`,`vehicle_type`,`model_name`,`model_year`,`fuel_type`,`vehicle_description`,`mileage`,`condition`,`sale_price`,`colors`) values(?, ?,?,?,?, ?, ?,?,?,?, ?)";

            // init value
            double salePrice = dto.getBuyPrice().doubleValue() * (1 + 0.25);

            jdbcTemplate.update(
                    insertSql, dto.getVin(), dto.getManufacturerName(), dto.getVehicleType(),
                    dto.getModelName(), dto.getModelYear(), dto.getFuelType(), dto.getVehicleDescription(),dto.getMileage(), dto.getCondition(), new BigDecimal(salePrice), dto.getColors());

            //inventory_transaction
            String insertInventoryTransaction = "insert into inventory_transaction(`vin`, `seller_id`, `inventory_clerk`,`buy_date`, `buy_price`, `sale_status`) values(?,?,?,?,?,?)";
            jdbcTemplate.update(insertInventoryTransaction, dto.getVin(), dto.getSellerId(), dto.getInventoryClerk(), dto.getBuyDate(), dto.getBuyPrice(),"forbidden");
            cnt = 1;
        }
        return cnt;
    }

    @Override
    public VehicleVo getVehicleByVin(String vin) {
        String sql = "select * from vehicle where vin=?";
        List<VehicleVo> list = jdbcTemplate.query(sql, VehicleServiceImpl::mapRow, vin);

        if(!list.isEmpty()){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public int updateSalePrice(String vin, BigDecimal updateSalePrice) {
        String updateSql = "update vehicle set sale_price=? where vin=?";
        return jdbcTemplate.update(updateSql, updateSalePrice, vin);
    }
}
