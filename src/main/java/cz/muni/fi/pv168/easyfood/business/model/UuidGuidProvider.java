package cz.muni.fi.pv168.easyfood.business.model;

import java.util.UUID;

public class UuidGuidProvider implements GuidProvider {

    @Override
    public String newGuid() {
        return UUID.randomUUID().toString();
    }
}
