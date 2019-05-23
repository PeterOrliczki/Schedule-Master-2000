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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static Logger logger = LoggerFactory.getLogger(TasksServlet.class);
    private static Logger exceptionLogger = LoggerFactory.getLogger(TasksServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = getConnection(request.getServletContext())) {
            TaskDao taskDao = new DatabaseTaskDao(connection);
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            TaskService taskService = new SimpleTaskService(taskDao, scheduleDao);

            User user = (User) request.getSession().getAttribute("user");
            List<Task> tasks = taskService.findAllByTaskId(user.getId());

            logger.info("Loaded " + tasks.size() + " tasks.");
            sendMessage(response, HttpServletResponse.SC_OK, tasks);
        } catch (SQLException exc) {
            logger.error("Exception occurred while processing request - For more information see the exception log file.");
            exceptionLogger.error("SQL exception occurred at: ", exc);
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

            logger.info("Created new task by user " + user.getName() + ", with task ID " + task.getId() + ".");
            sendMessage(response, HttpServletResponse.SC_OK, "Task successfully added");
        } catch (SQLException exc) {
            logger.error("Exception occurred while processing request - For more information see the exception log file.");
            exceptionLogger.error("SQL exception occurred at: ", exc);
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

            logger.info("Updated task ID " + task.getId() + ".");
            sendMessage(resp, HttpServletResponse.SC_OK, "Task updated.");
        } catch (SQLException exc) {
            logger.error("Exception occurred while processing request - For more information see the exception log file.");
            exceptionLogger.error("SQL exception occurred at: ", exc);
            handleSqlError(resp, exc);
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = getConnection(request.getServletContext())) {
            TaskDao taskDao = new DatabaseTaskDao(connection);
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            TaskService taskService = new SimpleTaskService(taskDao, scheduleDao);

            int id = Integer.valueOf(request.getParameter("id"));

            if (taskService.doesRelationExistByTaskId(id)) {
                taskService.deleteRelationRecordByTaskId(id);
            }
            taskService.deleteTaskById(id);

            logger.info("Deleted task ID " + id + ".");
            sendMessage(response, HttpServletResponse.SC_OK, "Task succesfully deleted");
        } catch (SQLException exc) {
            logger.error("Exception occurred while processing request - For more information see the exception log file.");
            exceptionLogger.error("SQL exception occurred at: ", exc);
            handleSqlError(response, exc);
        }
    }

}
