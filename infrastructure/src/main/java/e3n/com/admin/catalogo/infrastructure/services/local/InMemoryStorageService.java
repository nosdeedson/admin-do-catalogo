package e3n.com.admin.catalogo.infrastructure.services.local;

import com.E3N.admin.catalogo.domain.video.Resource;
import e3n.com.admin.catalogo.infrastructure.services.StorageService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStorageService implements StorageService {

    private final Map<String, Resource> storage;

    public InMemoryStorageService() {
        this.storage = new ConcurrentHashMap<>();
    }

    public void clear(){
        this.storage.clear();
    }

    public Map<String, Resource> storage(){
        return storage;
    }

    @Override
    public void store(String id, Resource resource) {
        this.storage.put(id, resource);
    }

    @Override
    public Optional<Resource> get(String id) {
        return Optional.ofNullable(this.storage.get(id));
    }

    @Override
    public List<String> list(String prefix) {
        return this.storage.keySet().stream()
                .filter(it -> it.startsWith(prefix))
                .toList();
    }

    @Override
    public void deleteAll(List<String> ids) {
        ids.forEach(this.storage::remove);
    }
}
