package com.github.cosysoft.device.shell;

import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.exec.environment.EnvironmentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ShellCommand {

	private static final Logger log = LoggerFactory.getLogger(ShellCommand.class
			.getName());

	public static String exec(CommandLine commandLine)
			throws ShellCommandException {
		return exec(commandLine, 20000);
	}

	public static String exec(CommandLine commandline, long timeoutInMillies)
			throws ShellCommandException {
		log.debug("executing command: " + commandline);
		PritingLogOutputStream outputStream = new PritingLogOutputStream();
		DefaultExecutor exec = new DefaultExecutor();
		exec.setWatchdog(new ExecuteWatchdog(timeoutInMillies));
		PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
		exec.setStreamHandler(streamHandler);
		try {
			exec.execute(commandline);
		} catch (Exception e) {
			throw new ShellCommandException(
					"An error occured while executing shell command: "
							+ commandline, new ShellCommandException(
							outputStream.getOutput()));
		}
		return (outputStream.getOutput());
	}

	public static void execAsync(CommandLine commandline)
			throws ShellCommandException {
		execAsync(null, commandline);
	}

	@SuppressWarnings("rawtypes")
	public static void execAsync(String display, CommandLine commandline)
			throws ShellCommandException {
		log.debug("executing async command: " + commandline);
		DefaultExecutor exec = new DefaultExecutor();

		ExecuteResultHandler handler = new DefaultExecuteResultHandler();
		PumpStreamHandler streamHandler = new PumpStreamHandler(
				new PritingLogOutputStream());
		exec.setStreamHandler(streamHandler);
		try {
			if (display == null || display.isEmpty()) {
				exec.execute(commandline, handler);
			} else {
				Map env = EnvironmentUtils.getProcEnvironment();
				EnvironmentUtils.addVariableToEnvironment(env, "DISPLAY=:"
						+ display);

				exec.execute(commandline, env, handler);
			}
		} catch (Exception e) {
			throw new ShellCommandException(
					"An error occured while executing shell command: "
							+ commandline, e);
		}
	}

	private static class PritingLogOutputStream extends LogOutputStream {

		private StringBuilder output = new StringBuilder();

		@Override
		protected void processLine(String line, int level) {
			log.debug("OUTPUT FROM PROCESS: " + line);

			output.append(line).append("\n");
		}

		public String getOutput() {
			return output.toString();
		}
	}
}
