package com.example.warehouseapp.item;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item addItem(ItemRequest request) {
        Item item = new Item(request.getName(), request.getDescription(), request.getQuantity());
        return itemRepository.save(item);
    }

    public Item updateItem(Long id, ItemRequest request) {
        Item existing = itemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Item not found"));
        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setQuantity(request.getQuantity());
        return itemRepository.save(existing);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
