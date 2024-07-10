package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.EntryDao;
import org.yearup.models.Entry;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlEntryDao extends MySqlDaoBase implements EntryDao {
    @Autowired
    public MySqlEntryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Entry> searchEntries(String description, String vendor, BigDecimal minAmount, BigDecimal maxAmount) {
        List<Entry> entries = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM entry WHERE 1=1");

        if (description != null && !description.isEmpty()) {
            query.append(" AND description LIKE ?");
        }
        if (vendor != null && !vendor.isEmpty()) {
            query.append(" AND vendor LIKE ?");
        }
        if (minAmount != null) {
            query.append(" AND amount >= ?");
        }
        if (maxAmount != null) {
            query.append(" AND amount <= ?");
        }

        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query.toString());

            int paramIndex = 1;
            if (description != null && !description.isEmpty()) {
                ps.setString(paramIndex++, "%" + description + "%");
            }
            if (vendor != null && !vendor.isEmpty()) {
                ps.setString(paramIndex++, "%" + vendor + "%");
            }
            if (minAmount != null) {
                ps.setBigDecimal(paramIndex++, minAmount);
            }
            if (maxAmount != null) {
                ps.setBigDecimal(paramIndex++, maxAmount);
            }

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Entry entry = mapRow(resultSet);
                entries.add(entry);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entries;
    }


    private Entry mapRow(ResultSet row) throws SQLException {
        int entryId = row.getInt("entry_id");
        String description = row.getString("description");
        String vendor = row.getString("vendor");
        BigDecimal amount = row.getBigDecimal("amount");

        return new Entry(entryId, description, vendor, amount);
    }


    @Override
    public Entry getEntryById(int id) {
        String query = "SELECT * FROM entries WHERE entry_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);

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
    public Entry createEntry(Entry entry) {
//TODO Figure out what information I need to insert into the database. Make date and time optional.
        String createSQL = "INSERT INTO entries (entry_id, description, vendor, amount) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(createSQL, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, entry.getEntryId());
            ps.setString(2, entry.getDescription());
            ps.setString(3, entry.getVendor());
            ps.setBigDecimal(4, entry.getAmount());

            ps.executeUpdate();

            ResultSet newEntryKey = ps.getGeneratedKeys();

            if (newEntryKey.next()) {
                return getEntryById(newEntryKey.getInt("entry_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void updateEntry(int entryId, Entry entry) {
//TODO Make sure the proper fields are getting updated.
        String sql = "UPDATE entries" +
                " SET description = ? " +
                "   , vendor = ? " +
                "   , amount = ? " +
                " WHERE entry_id = ?;";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, entry.getDescription());
            statement.setString(2, entry.getVendor());
            statement.setBigDecimal(3, entry.getAmount());
            statement.setInt(4, entryId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteEntry(int entryId) {
        String deleteSQL = "DELETE FROM entries WHERE entry_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(deleteSQL);

            ps.setInt(1, entryId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


