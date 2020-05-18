<?php
/**
 * The base configuration for WordPress
 *
 * The wp-config.php creation script uses this file during the
 * installation. You don't have to use the web site, you can
 * copy this file to "wp-config.php" and fill in the values.
 *
 * This file contains the following configurations:
 *
 * * MySQL settings
 * * Secret keys
 * * Database table prefix
 * * ABSPATH
 *
 * @link https://wordpress.org/support/article/editing-wp-config-php/
 *
 * @package WordPress
 */

// ** MySQL settings - You can get this info from your web host ** //
/** The name of the database for WordPress */
define( 'DB_NAME', 'wordpress' );

/** MySQL database username */
define( 'DB_USER', 'admin' );

/** MySQL database password */
define( 'DB_PASSWORD', 'admin' );

/** MySQL hostname */
define( 'DB_HOST', 'localhost' );

/** Database Charset to use in creating database tables. */
define( 'DB_CHARSET', 'utf8mb4' );

/** The Database Collate type. Don't change this if in doubt. */
define( 'DB_COLLATE', '' );

/**#@+
 * Authentication Unique Keys and Salts.
 *
 * Change these to different unique phrases!
 * You can generate these using the {@link https://api.wordpress.org/secret-key/1.1/salt/ WordPress.org secret-key service}
 * You can change these at any point in time to invalidate all existing cookies. This will force all users to have to log in again.
 *
 * @since 2.6.0
 */
define( 'AUTH_KEY',         '<Tl}>JGhwk#VQjQqHf9K_wANOMX_a2DHVsC N(%.;g&YmI/J|` sOzX`}7pcn{ZF' );
define( 'SECURE_AUTH_KEY',  'XW*Yl+7U^H9`|+GG;.iGMC^IriWkgnAFwUw((/Q=b=Cur-3mQpt/&Ng3l<TQn9_]' );
define( 'LOGGED_IN_KEY',    'Os:+#4jxAqk_OIfw8M7O}Kv%F*p!n18 Q</t>CA]n[TVRnQ gyU662*D(te2XhHh' );
define( 'NONCE_KEY',        'tAQj/$B~-VC+>lWHo9JeR/e@~M_@3MsD3=jJgdTbHxbf^9:X !1jJR6b<K-}o$ls' );
define( 'AUTH_SALT',        'Y@`rIU.km3;QK1k0ff3J$o7o.`QBt-0Dy1}2DfX,(DvMRH2t-+lp+e?W1]T+5Mr(' );
define( 'SECURE_AUTH_SALT', '_SP<]k$KI)DSv.jv`AzH{D4;*plj@|B4vU&;ez:VTc?!=ZhtUrdR%&bfD>kHkCa.' );
define( 'LOGGED_IN_SALT',   'gn03|,jjo7G`9n]G(;~y70hoLZ:UFlDnFH[`9w/R9{-W3:rO`T4a.y9|`6@U63(3' );
define( 'NONCE_SALT',       ']};v%%?laLfeuLXwYm[5%wae}#FjuVK<h2TkBAFN0Qq:d6T>;pN|J^G|qxe b&KE' );

/**#@-*/

/**
 * WordPress Database Table prefix.
 *
 * You can have multiple installations in one database if you give each
 * a unique prefix. Only numbers, letters, and underscores please!
 */
$table_prefix = 'wp_';

/**
 * For developers: WordPress debugging mode.
 *
 * Change this to true to enable the display of notices during development.
 * It is strongly recommended that plugin and theme developers use WP_DEBUG
 * in their development environments.
 *
 * For information on other constants that can be used for debugging,
 * visit the documentation.
 *
 * @link https://wordpress.org/support/article/debugging-in-wordpress/
 */
define( 'WP_DEBUG', false );

/* That's all, stop editing! Happy publishing. */

/** Absolute path to the WordPress directory. */
if ( ! defined( 'ABSPATH' ) ) {
	define( 'ABSPATH', __DIR__ . '/' );
}

/** Sets up WordPress vars and included files. */
require_once ABSPATH . 'wp-settings.php';
