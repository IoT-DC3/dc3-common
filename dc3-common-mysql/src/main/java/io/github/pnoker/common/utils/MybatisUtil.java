/*
 * Copyright 2016-present the IoT DC3 original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.pnoker.common.utils;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import io.github.pnoker.common.constant.common.ExceptionConstant;
import lombok.extern.slf4j.Slf4j;

import java.sql.Types;

/**
 * Mybatis 工具类集合
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
public class MybatisUtil {

    private MybatisUtil() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    /**
     * 默认的全局配置
     *
     * @param builder {@link GlobalConfig.Builder}
     * @param path    生成文件的根目录
     */
    public static void defaultGlobalConfig(GlobalConfig.Builder builder, String path) {
        builder.outputDir(path + "/java")
                .author("pnoker")
                .disableOpenDir();
    }

    /**
     * 默认的数据源配置
     *
     * @param builder {@link DataSourceConfig.Builder}
     */
    public static void defaultDataSourceConfig(DataSourceConfig.Builder builder) {
        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
            if (typeCode == Types.SMALLINT) {
                return DbColumnType.INTEGER;
            }
            return typeRegistry.getColumnType(metaInfo);
        });
    }

    /**
     * 默认的生成策略配置
     *
     * @param builder {@link StrategyConfig.Builder}
     */
    public static void defaultStrategyConfig(StrategyConfig.Builder builder) {
        builder.addTablePrefix("dc3_")
                .entityBuilder()
                .idType(IdType.ASSIGN_ID)
                .enableLombok()
                .formatFileName("%sDO")
                .enableTableFieldAnnotation()
                .enableRemoveIsPrefix()
                .enableFileOverride()
                .logicDeleteColumnName("deleted")
                .serviceBuilder()
                .formatServiceFileName("%sManager")
                .formatServiceImplFileName("%sManagerImpl")
                .enableFileOverride()
                .mapperBuilder()
                .enableFileOverride();
    }

}
