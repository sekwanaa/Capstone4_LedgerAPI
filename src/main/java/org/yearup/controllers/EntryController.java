package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.EntryDao;
import org.yearup.models.Entry;

import java.util.List;

// http://localhost:8080/entries
@RestController
@RequestMapping("entries")
@CrossOrigin
public class EntryController
{
    private final EntryDao entryDao;

    @Autowired
    public EntryController(EntryDao entryDao) {
        this.entryDao = entryDao;
    }

//@TODO Make this work with entries, not categories.
    @GetMapping("")
    public List<Entry> getAll()
    {
        return entryDao.searchEntries();
    }

//@TODO change this to a proper id stuff
    @GetMapping("{categoryId}")
    public ResponseEntity<Entry> getById(@PathVariable int categoryId)
    {
        Entry category = entryDao.getEntryById(categoryId);
        if (category == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(category, HttpStatus.OK);
        }
    }

//@TODO We don't need this method
    // https://localhost:8080/entries/1/products
    @GetMapping("{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        try {
            return productDao.listByCategoryId(categoryId);
        } catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't find the category you're looking for...");
        }
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Entry addCategory(@RequestBody Entry category)
    {
        try {
            return entryDao.createEntry(category);
        } catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Add category didn't work");
        }
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateCategory(@PathVariable int categoryId, @RequestBody Entry category)
    {
        try {
            entryDao.updateEntry(categoryId, category);
        } catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Update category didn't work");
        }
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCategory(@PathVariable int categoryId)
    {
        try {
            var entry = entryDao.getEntryById(categoryId);

            if (entry == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            entryDao.deleteEntry(categoryId);
        }
        catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Delete category didn't work!");
        }
    }
}
