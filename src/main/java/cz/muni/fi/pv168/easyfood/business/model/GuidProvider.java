package cz.muni.fi.pv168.easyfood.business.model;

/**
 * Provider of globally unique identifiers for new entities.
 *
 * The returned GUID should be globally unique. The provider will always
 * return a new identifier, which has not been used yet.
 */
public interface GuidProvider {

    String newGuid();
}
