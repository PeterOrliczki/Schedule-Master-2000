package com.codecool.web.servlet;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.TaskDao;
import com.codecool.web.dao.database.DatabaseScheduleDao;
import com.codecool.web.dao.database.DatabaseTaskDao;
import com.codecool.web.dto.RelationDto;
import com.codecool.web.model.Task;
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
            Task task = taskService.findTaskById(id);

            sendMessage(response, HttpServletResponse.SC_OK, task);
        } catch (SQLException exc) {
            handleSqlError(response, exc);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = getConnection(request.getServletContext())) {
            TaskDao taskDao = new DatabaseTaskDao(connection);
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            TaskService taskService = new SimpleTaskService(taskDao, scheduleDao);

            int taskId = Integer.valueOf(request.getParameter("taskId"));
            int scheduleId = Integer.valueOf(request.getParameter("scheduleId"));
            int columnNumber = Integer.valueOf(request.getParameter("columnNumber"));
            if (taskService.doesRelationExistByTaskAndScheduleId(taskId, scheduleId)) {
                RelationDto relationDto = new RelationDto(taskId, columnNumber, scheduleId);
                sendMessage(response, HttpServletResponse.SC_OK, relationDto);
            } else {
                sendMessage(response, HttpServletResponse.SC_OK, "False");
            }

        } catch (SQLException exc) {
            handleSqlError(response, exc);
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = getConnection(request.getServletContext())) {
            TaskDao taskDao = new DatabaseTaskDao(connection);
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            TaskService taskService = new SimpleTaskService(taskDao, scheduleDao);

            int taskId = Integer.valueOf(request.getParameter("taskId"));
            int scheduleId = Integer.valueOf(request.getParameter("scheduleId"));
            int columnNumber = Integer.valueOf(request.getParameter("columnNumber"));
            taskService.deleteRelationRecordByTaskAndScheduleId(taskId, scheduleId);
            RelationDto relationDto = new RelationDto(taskId, columnNumber, scheduleId);
            sendMessage(response, HttpServletResponse.SC_OK, relationDto);
        } catch (SQLException exc) {
            handleSqlError(response, exc);
        }
    }

}
