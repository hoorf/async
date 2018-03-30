package com.hrf.core;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.hrf.core.Task.TaskStaus;

public abstract class AbstractTaskManager {

	private final static ConcurrentLinkedQueue<Task> queue = new ConcurrentLinkedQueue<>();

	protected abstract List<Task> load();

	public void init() {
		queue.addAll(load());
	}

	public static Integer getProcess(String key) {
		return getTaskByKey(key).getProcess();
	}

	public static void cancelTask(String key) {
		getTaskByKey(key).setStatus(TaskStaus.CANCEL);
	}

	private static Task getTaskByKey(String key) {
		Task result = queue.stream().filter(x -> key.equals(x.getTaskKey())).findFirst().orElse(null);
		if (result == null) {
			throw new IllegalArgumentException("task ²»´æÔÚ");
		}
		return result;
	}

}
