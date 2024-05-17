package org.example.authservice.model.response;

import org.example.authservice.model.Logs;

import java.util.List;

public class LogsResponse {
	private List<Logs> logs;

	public LogsResponse(List<Logs> logs) {
		this.logs = logs;
	}

	public List<Logs> getLogs() {
		return logs;
	}

	public void setLogs(List<Logs> logs) {
		this.logs = logs;
	}
}
