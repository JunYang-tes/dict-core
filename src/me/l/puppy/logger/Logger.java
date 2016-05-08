package me.l.puppy.logger;

public abstract class Logger {
	public static Logger logger = new ConsoleLogger();

	public void warn(String msg, String[] stack) {
		warn_(format("WARN", msg, stack));
	}

	public void error(String msg, String[] stack) {
		error_(format("ERROR", msg, stack));
	}

	protected String format(String level, String msg, String[] stack) {
		StringBuilder sb = new StringBuilder();
		sb.append("[" + level + "] ");
		sb.append(msg + "\n");
		if (stack != null) {
			for (String s : stack) {
				sb.append("\t" + s + "\n");
			}
		}
		return sb.toString();
	}

	protected abstract void warn_(String msg);

	protected abstract void error_(String msg);

	public static void W(String msg) {
		Logger.logger.warn(msg, null);
	}

	public static void W(String msg, String[] stack) {
		Logger.logger.warn(msg, stack);
	}

	public static void E(String msg) {
		Logger.logger.error(msg, null);
	}

	public static void E(String msg, String[] stack) {
		Logger.logger.error(msg, stack);
	}

}
