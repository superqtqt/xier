#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**

 *
 * @author superqtqt 2021/6/4
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RedisKeyPrefix {

    public static final String BG_PREFIX = "us_bg_";

    public static final String IDEX_CLUSTER_PREFIX = "idex_cluster_";
}
