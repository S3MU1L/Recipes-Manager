package cz.fi.muni.pv168.easyfood.bussiness.model;

/**
 * Provider of globally unique identifiers for new entities.
 *
 * The returned GUID should be globally unique. The provider will always
 * return a new identifier, which has not been used yet.
 */
public interface GuidProvider {

    String newGuid();
}
