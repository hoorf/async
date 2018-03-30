package com.hrf.core;

import java.util.Date;

public class Task {

	private String taskKey;

	private String parentTaskKey;

	private String name;

	private TaskStaus status;

	private Date start;

	private Date end;

	private volatile Integer process;

	public String getTaskKey() {
		return taskKey;
	}

	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Integer getProcess() {
		return process;
	}

	public void setProcess(Integer process) {
		this.process = process;
	}

	public String getParentTaskKey() {
		return parentTaskKey;
	}

	public void setParentTaskKey(String parentTaskKey) {
		this.parentTaskKey = parentTaskKey;
	}

	public TaskStaus getStatus() {
		return status;
	}

	public void setStatus(TaskStaus status) {
		this.status = status;
	}

	enum TaskStaus {
		RUNNING, CANCEL, FINISH;
	}

}
