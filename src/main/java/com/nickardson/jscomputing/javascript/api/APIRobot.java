package com.nickardson.jscomputing.javascript.api;

public class APIRobot {

    public static RobotJSAPI create() {
        return new RobotJSAPI();
    }

    public static class RobotJSAPI {

        private RobotJSAPI() {

        }

        public void forward() {
            System.out.println("Moving forward");
        }
    }
}
