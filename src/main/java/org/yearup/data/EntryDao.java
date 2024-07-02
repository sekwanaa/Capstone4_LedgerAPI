package org.yearup.data;

import org.yearup.models.Entry;
import java.util.List;

public interface EntryDao
{
    List<Entry> getAllCategories();
    Entry getById(int categoryId);
    Entry create(Entry category);
    void update(int categoryId, Entry category);
    void delete(int categoryId);
}
