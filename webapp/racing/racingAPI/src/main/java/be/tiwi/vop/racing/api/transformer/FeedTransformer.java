package be.tiwi.vop.racing.api.transformer;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import be.tiwi.vop.racing.model.Circuit;
import be.tiwi.vop.racing.model.Event;
import be.tiwi.vop.racing.model.Ghost;
import be.tiwi.vop.racing.model.Obstacle;
import be.tiwi.vop.racing.model.Race;
import be.tiwi.vop.racing.model.Result;
import be.tiwi.vop.racing.model.Tile;
import be.tiwi.vop.racing.model.Tournament;
import be.tiwi.vop.racing.model.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FeedTransformer {
  public static String userFeed(List<User> feedData) {
    String feeds = null;
    Gson gson = new Gson();
    feeds = gson.toJson(feedData);
    return feeds;
  }

  public static String getUser(User user) {
    Gson gson = new Gson();
    return gson.toJson(user);
  }

  public static String getSHA512(String plain) {
    return DigestUtils.sha512Hex(plain);
  }

  public static String circuitFeed(List<Circuit> feedData) {
    Gson gson = new Gson();
    return gson.toJson(feedData);
  }

  public static String getCircuit(Circuit circuit) {
    Gson gson = new Gson();
    return gson.toJson(circuit);
  }

  public static String tilesFeed(List<Tile> feedData) {
    return new Gson().toJson(feedData);
  }

  public static String ghostsFeed(List<Ghost> feedData) {
    return new Gson().toJson(feedData);
  }

  public static String getGhost(Ghost ghost) {
    return new Gson().toJson(ghost);
  }

  public static String getTournament(Tournament tournament) {
    Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
    return gson.toJson(tournament);
  }

  public static String getTournaments(ArrayList<Tournament> tournaments) {
    Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
    return gson.toJson(tournaments);
  }

  public static String getRace(Race race) {
    return new Gson().toJson(race);
  }

  public static String getRaces(ArrayList<Race> races) {
    return new Gson().toJson(races);
  }

  public static String getResult(Result result) {
    return new Gson().toJson(result);
  }

  public static String getResults(ArrayList<Result> results) {
    return new Gson().toJson(results);
  }

  public static String obstaclesFeed(List<Obstacle> feedData) {
    return new Gson().toJson(feedData);
  }

  public static String eventFeed(List<Event> feedData) {
    Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm").create();
    return gson.toJson(feedData);
  }
}
