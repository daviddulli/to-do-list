package org.fasttrackit.persistance;

import org.fasttrackit.domain.Task;
import org.fasttrackit.transfer.CreateTaskRequest;
import org.fasttrackit.transfer.UpdateTaskRequest;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    public void createTask(CreateTaskRequest request) throws IOException, SQLException {
            //PREVENTING SQL INJECTION BY AVOIDING CONCATENATION and using PreparedStatement
        String sql = "INSERT INTO task (description, deadline) VALUES (?,?)";

        try (Connection connection = DatabaseConfiguration.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, request.getDescription());
            preparedStatement.setDate(2, Date.valueOf(request.getDeadline()));
            preparedStatement.executeUpdate();
        }

    }

    public void updateTask(int i, UpdateTaskRequest request) throws IOException, SQLException {
        //PREVENTING SQL INJECTION BY AVOIDING CONCATENATION and using PreparedStatement
        String sql = "UPDATE task SET done = ? WHERE ID = ?";

        try (Connection connection = DatabaseConfiguration.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setBoolean(1, request.isDone());
            preparedStatement.setLong(2, i);

            preparedStatement.executeUpdate();
        }

    }


    public List<Task> getTask() throws IOException, SQLException {
        String sql = "SELECT id, description, deadline, done FROM task ";

        try (Connection connection = DatabaseConfiguration.getConnection();
             //Statement should be used only for no parameter queries. works for read not write.
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)){

            List<Task> tasks = new ArrayList<>();
            while (resultSet.next()){
                Task task = new Task();
                task.setId(resultSet.getLong("id"));
                task.setDescription(resultSet.getString("description"));
                task.setDeadline(resultSet.getDate("deadline").toLocalDate());
                task.setDone(resultSet.getBoolean("done"));
                tasks.add(task);

            }

            return tasks;
        }

    }

}
