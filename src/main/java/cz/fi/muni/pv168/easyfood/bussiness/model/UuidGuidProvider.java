package cz.fi.muni.pv168.easyfood.bussiness.model;

import java.util.UUID;

public class UuidGuidProvider implements GuidProvider {

    @Override
    public String newGuid() {
        return UUID.randomUUID().toString();
    }
}
