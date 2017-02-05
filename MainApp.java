package gui;

import java.io.File;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class MainApp extends Application {
	private MediaPlayer player;

	private ListView<File> folders;
	private ListView<File> music;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) {
		stage.setTitle("Audio testing program");
		GridPane pane = new GridPane();
		initContent(pane);

		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.show();
	}

	private void initContent(GridPane pane) {
		pane.setVgap(10);
		pane.setHgap(10);

		folders = new ListView<File>();
		pane.add(folders, 0, 1);

		Label lblFolders = new Label("Folders");
		pane.add(lblFolders, 0, 0);

		music = new ListView<File>();
		pane.add(music, 1, 1, 4, 1);

		Label lblMusic = new Label("Music");
		pane.add(lblMusic, 1, 0);

		folders.getItems().setAll(scanDirForDirectories("C:\\Users\\Light\\Music\\iTunes\\iTunes Media\\Music"));

		ChangeListener<File> directoriesListener = (ov, oldFile, newFile) -> fileListenerAction();
		folders.getSelectionModel().selectedItemProperty().addListener(directoriesListener);

		Button btnPlay = new Button("Play");
		pane.add(btnPlay, 1, 2);
		btnPlay.setOnAction(event -> playAction());

		Button btnStop = new Button("Stop");
		pane.add(btnStop, 2, 2);
		btnStop.setOnAction(event -> stopAction());

		Button btnPause = new Button("Pause");
		pane.add(btnPause, 3, 2);
		btnPause.setOnAction(event -> pauseAction());

		Button btnStartFromPause = new Button("Start from pause");
		pane.add(btnStartFromPause, 4, 2);
		btnStartFromPause.setOnAction(event -> startFromPauseSongAction());
	}

	private void pauseAction() {
		if (player != null) {
			player.pause();
		}
	}

	private void startFromPauseSongAction() {
		if (player != null) {
			player.play();
		}
	}

	private void playAction() {
		String file = music.getSelectionModel().getSelectedItem().getPath();
		String file2 = folders.getSelectionModel().getSelectedItem().getPath();

		if (file != null && file.length() > 0 && file2 != null && file2.length() > 0) {
			File mFile = new File(file2 + "\\" + file);

			if (file.substring(file.length() - 4).equalsIgnoreCase(".m4a")) {
				Media media = new Media(mFile.toURI().toString());

				if (player != null) {
					player.stop();
				}

				player = new MediaPlayer(media);
				player.play();
			}
		}
	}

	private void stopAction() {
		if (player != null) {
			player.stop();
		}
	}

	private void fileListenerAction() {
		File file = folders.getSelectionModel().getSelectedItem();

		if (file != null && file.isDirectory()) {
			music.getItems().setAll(scanDirForMusicFiles(file.toURI().toString()));
		} else {
			music.getItems().clear();
		}
	}

	private ArrayList<File> scanDirForDirectories(String path) {
		ArrayList<File> directories = new ArrayList<>();

		// Create a File-object matching the folder with the given path
		File file = new File(path);

		if (file != null) {
			directories.add(file);
		}

		// Fetch the list of all files and sub-folders
		if (file != null && file.isDirectory()) {
			String[] names = file.list();

			if (names != null) {
				for (String name : names) {
					File file2 = new File(path + "\\" + name);
					if (file2.isDirectory()) {
						directories.addAll(scanDirForDirectories(path + "\\" + name + "\\"));
					}
				}
			}
		}

		return directories;
	}

	private ArrayList<File> scanDirForMusicFiles(String path) {
		ArrayList<File> files = new ArrayList<>();

		File file = this.folders.getSelectionModel().getSelectedItem();

		if (file != null) {
			String[] names = file.list();

			for (String name : names) {
				if (name.substring(name.length() - 4).equalsIgnoreCase(".m4a")) {
					File file2 = new File(name);
					files.add(file2);
				}
			}
		}

		return files;
	}

	// private void laterMethod() {
	// String s = "C:\\Users\\Light\\Dropbox\\01 Papercut.m4a";
	// File file = new File(s);
	// Media media = new Media(file.toURI().toString());
	// player = new MediaPlayer(media);
	// player.play();
	// }
}