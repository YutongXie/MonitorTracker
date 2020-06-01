package com.huitong.monitortracker.dao.mybatis;

import com.huitong.monitortracker.entity.NumberUpperLimitBreachResult;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface OutputProcessorMapper {
    @SelectProvider(type = MonitorTrackerSelectSqlProvider.class, method = "getLastResultSql")
    @Results({
            @Result(column = "SCHEMA_NAME", property = "schemaName"),
            @Result(column = "TABLE_NAME", property = "tableName"),
            @Result(column = "COLUMN_NAME", property = "columnName"),
            @Result(column = "COLUMN_DATA_TYPE", property = "columnDataType"),
            @Result(column = "ACTIVE", property = "active"),
            @Result(column = "CURRENT_VALUE", property = "currentValue"),
            @Result(column = "LIMIT_VALUE", property = "limitValue"),
            @Result(column = "CREATE_TIME", property = "createTime"),
            @Result(column = "UPDATE_TIME", property = "updateTime")
    })
    List<NumberUpperLimitBreachResult> getLastResult();

    @SelectProvider(type = MonitorTrackerSelectSqlProvider.class, method = "inactiveLastResultSql")
    void inactiveLastResult(@Param("schemaName") String schemaName);

    @SelectProvider(type = MonitorTrackerSelectSqlProvider.class, method = "saveSql")
    void save(@Param("resultList") List<NumberUpperLimitBreachResult> resultList);
}
