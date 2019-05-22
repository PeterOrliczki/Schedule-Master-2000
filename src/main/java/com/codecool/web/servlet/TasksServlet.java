package com.codecool.web.servlet;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.TaskDao;
import com.codecool.web.dao.database.DatabaseScheduleDao;
import com.codecool.web.dao.database.DatabaseTaskDao;
import com.codecool.web.model.Task;
import com.codecool.web.model.User;
import com.codecool.web.service.TaskService;
import com.codecool.web.service.simple.SimpleTaskService;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    private final ObjectMapper om = new ObjectMapper();

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
            int taskStart = Integer.parseInt(request.getParameter("task-begin"));
            int taskEnd = Integer.parseInt(request.getParameter("task-end"));

            Task task = taskService.addTask(userId, taskTitle, taskContent, taskStart, taskEnd);
            sendMessage(response, HttpServletResponse.SC_OK, "Task succesfully added");
        } catch (SQLException exc) {
            handleSqlError(response, exc);
        }
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            TaskDao taskDao = new DatabaseTaskDao(connection);
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            TaskService taskService = new SimpleTaskService(taskDao, scheduleDao);

            Task task = om.readValue(req.getInputStream(), Task.class);
            taskService.updateTitleById(task.getId(), task.getTitle());
            taskService.updateContentById(task.getId(), task.getContent());
            taskService.updateStartById(task.getId(), task.getStart());
            taskService.updateEndById(task.getId(), task.getEnd());

            sendMessage(resp, HttpServletResponse.SC_OK, "Task updated.");
        } catch (SQLException exc) {
            handleSqlError(resp, exc);
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = getConnection(request.getServletContext())) {
            TaskDao taskDao = new DatabaseTaskDao(connection);
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            TaskService taskService = new SimpleTaskService(taskDao, scheduleDao);

            User user = (User) request.getSession().getAttribute("user");

            int id = Integer.valueOf(request.getParameter("id"));

            if (taskService.doesRelationExistByTaskId(id)) {
                taskService.deleteRelationRecordByTaskId(id);
            }

            taskService.deleteTaskById(id);
            List<Task> tasks = taskService.findAllByTaskId(user.getId());

            sendMessage(response, HttpServletResponse.SC_OK, "Task succesfully deleted");
        } catch (SQLException exc) {
            handleSqlError(response, exc);
        }
    }

}
