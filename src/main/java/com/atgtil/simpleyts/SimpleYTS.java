package com.atgtil.simpleyts;

import com.atgtil.simpleyts.handler.SimpleYTSHandler;
import com.vtence.cli.CLI;
import com.vtence.cli.args.Args;
import ratpack.handling.Chain;
import ratpack.server.BaseDir;
import ratpack.server.RatpackServer;

import java.io.IOException;

import static java.net.InetAddress.getByName;
import static ratpack.server.RatpackServer.of;

public class SimpleYTS { private final String host;
    private final int port;

    private RatpackServer server;

    public SimpleYTS(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        server = of(server ->
                server.serverConfig(
                        serverConfig -> serverConfig.port(port).address(getByName(host))
                                .baseDir(BaseDir.find()))
                        .handlers(
                                chain -> {
                                    chain
                                            .get(new SimpleYTSHandler())

                                            .prefix("css", ctx -> ctx.fileSystem("css", Chain::files))
                                            .prefix("js", ctx -> ctx.fileSystem("js", Chain::files));
                                }
                        )
        );
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public static SimpleYTS launch(String host, int port) throws Exception {
        SimpleYTS dashboard = new SimpleYTS(host, port);
        dashboard.start();
        return dashboard;
    }

    public static void main(String... args) throws Exception {
        Args parsedArgs = parseArguments(args);
        launch(parsedArgs.get("--host"), parsedArgs.get("--port"));
    }

    private static Args parseArguments(String... args) throws IOException {
        CLI cli = new CLI() {{
            name("health-dashboard");
            description("java -jar health-dashboard-*.jar");

            option("-h", "--host HOST", "Host address that consul will use for its health check (default: 0.0.0.0)").defaultingTo("0.0.0.0");
            option("-p", "--port PORT", "Port to listen on (default: 9100)").ofType(int.class).defaultingTo(9090);
        }};

        try {
            return cli.parse(args);
        } catch (Exception ex) {
            cli.printHelp(System.out);
            System.exit(1);
            return new Args();
        }
    }
}
