package fi.metatavu.plastep.productinformation.test.functional.settings

/**
 * Settings implementation for test builder
 *
 * @author Antti Leppä
 */
class ApiTestSettings {

    companion object {

        /**
         * Returns API service base path
         */
        val apiBasePath: String
            get() = "$apiBasePathNoPort:$apiBasePort"

        /**
         * Returns API service base path
         */
        val apiBasePathNoPort: String
            get() = "http://localhost"

        /**
         * Returns API service port
         */
        val apiBasePort: Int
            get() = 8081
    }

}
