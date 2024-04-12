package io.github.pnoker.common.tdengine.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.pnoker.common.entity.bo.PointValueBO;
import io.github.pnoker.common.entity.common.Pages;
import io.github.pnoker.common.entity.query.PointValueQuery;
import io.github.pnoker.common.tdengine.entity.model.TDEnginePointValueDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@DS("taos")
public interface TDEngineRepositoryMapper extends BaseMapper<TDEnginePointValueDO> {
    void savePointValue(@Param("tableName")String tableName,@Param("pointValueDO") TDEnginePointValueDO pointValueDO);

    void saveBatchPointValue(@Param("tableName")String tableName,@Param("tdEnginePointValueDOS") List<TDEnginePointValueDO> tdEnginePointValueDOS);

    List<TDEnginePointValueDO> selectHistoryPointValue(@Param("deviceId")Long deviceId,@Param("pointId") Long pointId,@Param("count") int count);

    List<TDEnginePointValueDO> selectLatestPointValue(@Param("deviceId")Long deviceId,@Param("pointIds") List<Long> pointIds);

    long count(@Param("entityQuery")PointValueQuery entityQuery);

    List<TDEnginePointValueDO> selectPagePointValue(@Param("entityQuery")PointValueQuery entityQuery,@Param("pages") Pages pages);
}
