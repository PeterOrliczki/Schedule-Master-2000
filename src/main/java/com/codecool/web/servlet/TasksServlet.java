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
import java.util.List;


@WebServlet("/protected/tasks")
public class TasksServlet extends AbstractServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = getConnection(request.getServletContext())) {
            TaskDao taskDao = new DatabaseTaskDao(connection);
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            TaskService taskService = new SimpleTaskService(taskDao, scheduleDao);

            User user = (User) request.getSession().getAttribute("user");
            List<Task> tasks = taskService.findAllByTaskId(user.getId());

            sendMessage(response, HttpServletResponse.SC_OK, tasks);
        } catch (SQLException exc) {
            handleSqlError(response, exc);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = getConnection(request.getServletContext())) {
            TaskDao taskDao = new DatabaseTaskDao(connection);
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            TaskService taskService = new SimpleTaskService(taskDao, scheduleDao);

            User user = (User) request.getSession().getAttribute("user");

            int userId = user.getId();
            String taskTitle = request.getParameter("task-title");
            String taskContent = request.getParameter("task-content");
            int taskStart = Integer.valueOf(request.getParameter("task-start"));
            int taskEnd = Integer.valueOf(request.getParameter("task-end"));

            taskService.addTask(userId, taskTitle, taskContent, taskStart, taskEnd);

        } catch (SQLException exc) {
            handleSqlError(response, exc);
        }
    }

}
