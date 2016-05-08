package me.l.puppy.logger;

public class ConsoleLogger extends Logger {

	@Override
	protected void warn_(String msg) {
		System.out.println(msg);
	}

	@Override
	protected void error_(String msg) {
		System.err.println(msg);
	}
	
}
