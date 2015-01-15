package be.tiwi.vop.racing.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.core.model.Circuit;
import be.tiwi.vop.racing.core.model.Obstacle;
import be.tiwi.vop.racing.core.model.Tile;
import be.tiwi.vop.racing.web.service.client.CircuitServiceClient;

import com.google.gson.Gson;

@WebServlet("/CircuitServlet")
public class CircuitServlet extends HttpServlet {
  private static final Logger logger = LoggerFactory.getLogger(CircuitServlet.class);

  protected void getCircuitsByUserId(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Get request params
    String username = request.getSession().getAttribute("username").toString();
    String userId = request.getSession(true).getAttribute("userId").toString();
    String authString = request.getSession().getAttribute("authString").toString();
    request.setAttribute("username", username);

    List<Circuit> list =
        new CircuitServiceClient(authString).getCircuitsByUserId(Integer.parseInt(userId));
    logger.info("Number of circuits found: " + list.size());

    String json = new Gson().toJson(list);
    logger.info("json: " + json);
    response.getOutputStream().print(json);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String action = request.getParameter("action");
    if (action.equals("getCircuit")) {
      getCircuitById(request, response);
    } else if (action.equals("getTiles")) {
      getTilesByCircuitId(request, response);
    } else if (action.equals("getObstacles")) {
      getObstaclesByCircuitId(request, response);
    } else if (action.equals("favorite")) {
      favoriteCircuit(request, response);
    } else if (action.equals("unfavorite")) {
      unfavoriteCircuit(request, response);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String action = request.getParameter("action");
    if (action.equals("create")) {
      createCircuit(request, response);
    } else if (action.equals("update")) {
      updateCircuit(request, response);
    } else if (action.equals("get")) {
      getCircuitsByUserId(request, response);
    } else if (action.equals("favorites")) {
      getFavoriteCircuitsByUserId(request, response);
    }
  }

  protected void createCircuit(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // get parameters
    String authString = request.getSession().getAttribute("authString").toString();
    int userId = Integer.parseInt(request.getSession().getAttribute("userId").toString());
    String name = request.getParameter("name").toString();
    int rows = Integer.parseInt(request.getParameter("rows").toString());
    int columns = Integer.parseInt(request.getParameter("cols").toString());
    String direction = request.getParameter("direction").toString();
    String tiles = request.getParameter("tiles").toString();
    String obstacles = request.getParameter("obstacles").toString();
    logger.info(tiles);

    // request api
    CircuitServiceClient client = new CircuitServiceClient(authString);
    Circuit circuit = new Circuit();
    circuit.setDesigner(userId);
    circuit.setName(name);
    circuit.setRows(rows);
    circuit.setColumns(columns);
    Circuit.Direction dir = Circuit.Direction.valueOf(direction.toUpperCase());
    circuit.setDirection(dir);
    Circuit c = client.createCircuit(circuit, tiles, obstacles);

    // write response
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String json = new Gson().toJson(c);
    response.getWriter().write(json);
  }

  protected void updateCircuit(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // get parameters
    String authString = request.getSession().getAttribute("authString").toString();
    int userId = Integer.parseInt(request.getSession().getAttribute("userId").toString());
    String name = request.getParameter("name").toString();
    int rows = Integer.parseInt(request.getParameter("rows").toString());
    int columns = Integer.parseInt(request.getParameter("cols").toString());
    int circuitId = Integer.parseInt(request.getParameter("id").toString());
    String direction = request.getParameter("direction").toString();
    String tiles = request.getParameter("tiles").toString();
    String insertTiles = request.getParameter("insertTiles").toString();
    String deleteTiles = request.getParameter("deleteTiles").toString();
    String obstacles = request.getParameter("obstacles").toString();
    String updateObstacles = request.getParameter("update_obstacles").toString();
    String deleteObstacles = request.getParameter("delete_obstacles").toString();

    // request api
    CircuitServiceClient client = new CircuitServiceClient(authString);
    Circuit circuit = new Circuit();
    circuit.setId(circuitId);
    circuit.setDesigner(userId);
    circuit.setName(name);
    circuit.setRows(rows);
    circuit.setColumns(columns);
    Circuit.Direction dir = Circuit.Direction.valueOf(direction.toUpperCase());
    circuit.setDirection(dir);
    Circuit c =
        client.updateCircuit(circuit, tiles, insertTiles, deleteTiles, obstacles, updateObstacles,
            deleteObstacles);

    // write response
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String json = new Gson().toJson(c);
    response.getWriter().write(json);
  }

  protected void getCircuitById(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // get parameters
    String authString = request.getSession().getAttribute("authString").toString();
    int circuitId = Integer.parseInt(request.getParameter("id"));
    // api call
    CircuitServiceClient client = new CircuitServiceClient(authString);
    Circuit c = client.getCircuitByCircuitId(circuitId);

    // write response
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String json = new Gson().toJson(c);
    response.getWriter().write(json);
  }

  protected void getTilesByCircuitId(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // get parameters
    String authString = request.getSession().getAttribute("authString").toString();
    int circuitId = Integer.parseInt(request.getParameter("id"));
    // api call
    CircuitServiceClient client = new CircuitServiceClient(authString);
    List<Tile> t = client.getTilesByCircuitId(circuitId);

    // write response
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String json = new Gson().toJson(t);
    response.getWriter().write(json);
  }

  protected void getObstaclesByCircuitId(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // get parameters
    String authString = request.getSession().getAttribute("authString").toString();
    int circuitId = Integer.parseInt(request.getParameter("id"));
    // api call
    CircuitServiceClient client = new CircuitServiceClient(authString);
    List<Obstacle> t = client.getObstaclesByCircuitId(circuitId);

    // write response
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String json = new Gson().toJson(t);
    response.getWriter().write(json);
  }

  public void favoriteCircuit(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    // get parameters
    int id = Integer.parseInt(request.getParameter("circuitId"));
    String authString = request.getSession().getAttribute("authString").toString();
    int userId = Integer.parseInt(request.getSession().getAttribute("userId").toString());
    // request api
    CircuitServiceClient client = new CircuitServiceClient(authString);
    int status = client.favoriteCircuit(userId, id);
    // write response
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String json = new Gson().toJson(id);
    response.getWriter().write(status);
  }

  public void unfavoriteCircuit(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    // get parameters
    int id = Integer.parseInt(request.getParameter("circuitId"));
    String authString = request.getSession().getAttribute("authString").toString();
    int userId = Integer.parseInt(request.getSession().getAttribute("userId").toString());
    // request api
    CircuitServiceClient client = new CircuitServiceClient(authString);
    int status = client.unfavoriteCircuit(userId, id);
    // write response
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String json = new Gson().toJson(id);
    response.getWriter().write(status);
  }

  protected void getFavoriteCircuitsByUserId(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    // Get request params
    String username = request.getSession().getAttribute("username").toString();
    String userId = request.getSession(true).getAttribute("userId").toString();
    String authString = request.getSession().getAttribute("authString").toString();
    request.setAttribute("username", username);

    List<Circuit> list =
        new CircuitServiceClient(authString).getFavoriteCircuitsByUserId(Integer.parseInt(userId));
    logger.info("Number of circuits found: " + list.size());

    // write response
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String json = new Gson().toJson(list);
    logger.info("json: " + json);
    response.getWriter().write(json);
  }
}
