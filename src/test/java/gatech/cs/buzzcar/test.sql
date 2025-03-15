SELECT v.vehicle_type,
       CASE
           WHEN t2.AveDays IS NOT NULL THEN
               CAST(t2.AveDays AS CHAR)
           ELSE
               'N/A'
           END AS 'Average_Time_In_Inventory'
FROM (SELECT DISTINCT vehicle_type FROM Vehicle) v
         LEFT JOIN
     (SELECT v.vehicle_type,
             AVG(DATEDIFF(COALESCE(st.sale_date, CURDATE()), it.buy_date))
                 as AveDays
      FROM Vehicle v
               INNER JOIN Inventory_Transaction as it ON v.vin = it.vin
               LEFT JOIN Sale_Transaction as st ON it.vin = st.vin
      GROUP BY v.vehicle_type) as t2
     ON v.vehicle_type = t2.vehicle_type;



