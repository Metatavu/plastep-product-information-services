package fi.metatavu.plastep.productinformation.rest

/**
 * Abstract translator class
 *
 * @author Antti Leppä
 */
abstract class AbstractTranslator<E, R> {

    abstract fun translate(entity: E): R

    /**
     * Translates list of entities
     *
     * @param entities list of entities to translate
     * @return List of translated entities
     */
    open fun translate(entities: List<E>): List<R> {
        return entities.mapNotNull{ translate(it) }
    }

}