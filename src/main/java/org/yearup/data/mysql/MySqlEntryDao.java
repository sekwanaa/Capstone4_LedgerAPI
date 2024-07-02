package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.EntryDao;
import org.yearup.models.Entry;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//@TODO Fix this to make it work with Entries, not the categories.
@Component
public class MySqlEntryDao extends MySqlDaoBase implements EntryDao
{
    public MySqlEntryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Entry> getAllCategories()
    {
        List<Entry> categories = new ArrayList<>();
        String query = "SELECT * FROM categories";

        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Entry entry = mapRow(resultSet);
                categories.add(entry);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    @Override
    public Entry getById(int categoryId)
    {
        String query = "SELECT * FROM categories WHERE category_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, categoryId);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                return mapRow(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Entry create(Entry category)
    {
        String deleteSQL = "INSERT INTO categories (category_id, name, description) VALUES (?, ?, ?)";

        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(deleteSQL);

            ps.setInt(1, category.getCategoryId());
            ps.setString(2, category.getName());
            ps.setString(3, category.getDescription());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return category;
    }

    @Override
    public void update(int categoryId, Entry category)
    {
        String sql = "UPDATE categories" +
                " SET name = ? " +
                "   , description = ? " +
                " WHERE category_id = ?;";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int categoryId)
    {
        String deleteSQL = "DELETE FROM categories WHERE category_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(deleteSQL);

            ps.setInt(1, categoryId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Entry mapRow(ResultSet row) throws SQLException
    {
//@TODO Pull the correct information from the ResultSet
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        return new Entry()
        {{
//@TODO Set the proper fields in the Entry Model.
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};
    }

}
