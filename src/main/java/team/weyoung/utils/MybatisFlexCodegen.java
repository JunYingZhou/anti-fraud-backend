//package team.weyoung.utils;
//
//import com.mybatisflex.codegen.Generator;
//import com.mybatisflex.codegen.config.ColumnConfig;
//import com.mybatisflex.codegen.config.GlobalConfig;
//import com.mybatisflex.core.service.IService;
//import com.mybatisflex.spring.service.impl.ServiceImpl;
//import com.zaxxer.hikari.HikariDataSource;
//
///**
// * @author Tunan
// * @version 1.0
// * @since 2023/10/25
// */
//public class MybatisFlexCodegen {
//
//    public static void main(String[] args) {
//        //配置数据源
//        HikariDataSource dataSource = new HikariDataSource();
//        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/my_oj?characterEncoding=utf-8");
//        dataSource.setUsername("root");
//        dataSource.setPassword("261615");
//
//        //创建配置内容，两种风格都可以。
//        GlobalConfig globalConfig = createGlobalConfigUseStyle1();
//        //GlobalConfig globalConfig = createGlobalConfigUseStyle2();
//
//        //通过 datasource 和 globalConfig 创建代码生成器
//        Generator generator = new Generator(dataSource, globalConfig);
//
//        //生成代码
//        generator.generate();
//    }
//
//    public static GlobalConfig createGlobalConfigUseStyle1() {
//        //创建配置内容
//        GlobalConfig globalConfig = new GlobalConfig();
//
//        globalConfig.enableController();
//        globalConfig.enableService();
//        globalConfig.enableServiceImpl();
//        globalConfig.enableMapperXml();
//
//
//        //设置根包
//        globalConfig.setBasePackage("com.test");
//
//        //设置表前缀和只生成哪些表
//        //globalConfig.setTablePrefix("tb_");
//        globalConfig.setGenerateTable("question_answer");
//
//        //设置生成 entity 并启用 Lombok
//        globalConfig.setEntityGenerateEnable(true);
//        globalConfig.setEntityWithLombok(true);
//
//        //设置生成 mapper
//        globalConfig.setMapperGenerateEnable(true);
//
//        //设置生成controller
//        globalConfig.getControllerConfig()
//                //.setClassPrefix("My")
//                .setClassSuffix("Controller");
//                //.setSuperClass(BaseController.class);
//
//        //设置生成service
//        globalConfig.getServiceConfig()
//                .setClassPrefix("I")
//                .setClassSuffix("Service")
//                .setSuperClass(IService.class);
//        //设置生成serviceImpl
//        globalConfig.getServiceImplConfig()
//                //.setClassPrefix("My")
//                .setClassSuffix("ServiceImpl")
//                .setSuperClass(ServiceImpl.class);
//        //设置生成MapperXml
//        globalConfig.getMapperXmlConfig()
//                .setFilePrefix("My")
//                .setFileSuffix("Mapper");
//        //可以单独配置某个列
////        ColumnConfig columnConfig = new ColumnConfig();
////        columnConfig.setColumnName("tenant_id");
////        columnConfig.setLarge(true);
////        columnConfig.setVersion(true);
////        globalConfig.setColumnConfig("tb_account", columnConfig);
//
//        return globalConfig;
//    }
//
//    public static GlobalConfig createGlobalConfigUseStyle2() {
//        //创建配置内容
//        GlobalConfig globalConfig = new GlobalConfig();
//
//        //设置根包
//        globalConfig.getPackageConfig()
//                .setBasePackage("com.test");
//
//        //设置表前缀和只生成哪些表，setGenerateTable 未配置时，生成所有表
//        globalConfig.getStrategyConfig()
//                .setTablePrefix("tb_")
//                .setGenerateTable("tb_account", "tb_account_session");
//
//        //设置生成 entity 并启用 Lombok
//        globalConfig.enableEntity()
//                .setWithLombok(true);
//
//        //设置生成 mapper
//        globalConfig.enableMapper();
//
//        //可以单独配置某个列
//        ColumnConfig columnConfig = new ColumnConfig();
//        columnConfig.setColumnName("tenant_id");
//        columnConfig.setLarge(true);
//        columnConfig.setVersion(true);
//        globalConfig.getStrategyConfig()
//                .setColumnConfig("tb_account", columnConfig);
//
//        return globalConfig;
//    }
//}
