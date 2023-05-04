package me.konso.home_watchdog.entities.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * データベースの接続に利用される設定ファイル。
 */
@Serializable
data class DatabaseConfig(
    /**
     * データベース接続に用いるユーザー名。
     */
    @SerialName("database_user")
    val databaseUser: String,


    /**
     * データベース接続に用いるパスワード。
     * ここで利用するパスワードはSHA256でハッシュしたものを記述してください。
     */
    @SerialName("database_pass")
    val databasePass: String,


    /**
     * データベースに接続するためのドライバー。
     * 現在はPostgresのみ対応しています。
     */
    @SerialName("database_driver")
    val databaseDriver: String,

    /**
     * データベースに接続するためのURL。
     * データベース名を含めたURLで記述する必要があります。
     */
    @SerialName("database_url")
    val databaseUrl: String


)