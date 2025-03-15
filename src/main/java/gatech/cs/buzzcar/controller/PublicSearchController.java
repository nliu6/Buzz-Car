package gatech.cs.buzzcar.controller;

import gatech.cs.buzzcar.common.model.Result;
import gatech.cs.buzzcar.entity.param.PublicSearchParam;
import gatech.cs.buzzcar.entity.vo.VehicleVo;
import gatech.cs.buzzcar.service.VehicleService;
import io.swagger.annotations.Api;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
@Api(tags = "Public APIs")
@AllArgsConstructor
public class PublicSearchController {


    @Resource
    private VehicleService vehicleService;

    @PostMapping(value = "/search")
    public Result search(@RequestBody PublicSearchParam publicSearchParam){
        List<VehicleVo> vehicleVoList = vehicleService.search(publicSearchParam);
        return Result.successData(vehicleVoList);
    }

    @GetMapping(value = "/available-quantity-for-sale")
    public Result queryVehicleAvailableQuantityForSale(){
        int quantity = vehicleService.queryVehicleAvailableQuantityForSale();
        return Result.successData(quantity);
    }


}
