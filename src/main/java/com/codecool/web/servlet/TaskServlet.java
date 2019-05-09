package com.codecool.web.servlet;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.TaskDao;
import com.codecool.web.dao.database.DatabaseScheduleDao;
import com.codecool.web.dao.database.DatabaseTaskDao;
import com.codecool.web.model.Task;
import com.codecool.web.model.User;
import com.codecool.web.service.TaskService;
import com.codecool.web.service.simple.SimpleTaskService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


@WebServlet("/protected/task")
public class TaskServlet extends AbstractServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = getConnection(request.getServletContext())) {
            TaskDao taskDao = new DatabaseTaskDao(connection);
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            TaskService taskService = new SimpleTaskService(taskDao, scheduleDao);

            int id = Integer.valueOf(request.getParameter("id"));

            //sendMessage(response, HttpServletResponse.SC_OK, task);
        } catch (SQLException exc) {
            handleSqlError(response, exc);
        }
    }
}
