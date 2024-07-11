package tech.nmhillusion.mvn_buflo_builder.builder


/**
 * created by: nmhillusion
 * <p>
 * created date: 2024-07-11
 */
class BannerBuilder {

    val appBanner: String
        get() = StringBuilder("\n")
            .append("\n")
            .append("-----------------------------------------------------")
            .append("\n")
            .append("\tMVN Buflo Builder")
            .append("\n")
            .append("-----------------------------------------------------")
            .append("\n")
            .append("\n")
            .toString()

}