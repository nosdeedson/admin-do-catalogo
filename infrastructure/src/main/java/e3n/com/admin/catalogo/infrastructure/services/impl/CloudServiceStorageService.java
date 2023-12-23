package e3n.com.admin.catalogo.infrastructure.services.impl;

import com.E3N.admin.catalogo.domain.video.Resource;
import e3n.com.admin.catalogo.infrastructure.services.StorageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CloudServiceStorageService implements StorageService {
    // TODO IMPLEMENT USING S3 AWS
    @Override
    public void store(String id, Resource resource) {

    }

    @Override
    public Optional<Resource> get(String id) {
        return Optional.empty();
    }

    @Override
    public List<String> list(String prefix) {
        return new ArrayList<>(1);
    }

    @Override
    public void deleteAll(List<String> ids) {

    }
}
