package nu.westlin.fromspringbeansdependenciestoevents

import org.springframework.jdbc.core.simple.JdbcClient

fun rensaDatabasen(jdbcClient: JdbcClient) {
    jdbcClient.sql(
        """
        SELECT table_name 
            FROM information_schema.tables
            WHERE table_schema='public'
            AND table_type='BASE TABLE'
            AND table_name not like '%flyway%'
            order by table_schema
        """.trimIndent()
    )
        .query(String::class.java)
        .set()
        .forEach { tabell ->
            jdbcClient.sql("truncate $tabell").update()
        }
}