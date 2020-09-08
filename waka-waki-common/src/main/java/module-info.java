module waka.waki.common {
    exports com.manerfan.waka.waki.common.tools.snowflake;

    // use static for compile only but not runtime
    requires static lombok;
    requires org.apache.commons.lang3;
    requires io.vavr;
}