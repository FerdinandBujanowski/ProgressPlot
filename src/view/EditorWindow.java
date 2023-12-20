package view;

import java.io.*;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.*;

import control.tools.*;
import control.EditorInterface;
import control.command.*;
import control.selection.*;
import control.clipboard.Clipboard;
import control.clipboard.ClipboardListener;
import model.clip.Board;
import model.clip.Clip;

public class EditorWindow implements EditorInterface {

	//TODO change
	private Board board;

	private Canvas canvas;
	private Label statusLabel;

	private Selection selection;
	private CommandStack commandStack;

	private ToolRect toolRect = new ToolRect();
	private ToolEllipse toolEllipse = new ToolEllipse();
	private ToolImage toolImage = new ToolImage();
	private ToolSelection toolSelection = new ToolSelection();
	
	/**
	 * Variable pour l'outil courant
	 */
	private Tool currentTool;

	private String name = "Pinboard - ";
	
	private ClipboardListener clipboardListener;
	
	/**
	 * Constructeur
	 * @param stage une instance de la classe Stage
	 */
	public EditorWindow(Stage stage, Board board) {
		this.board = board;
		
		//titre
		stage.setTitle(this.name + "<untitled>");
		
		
		this.selection = new Selection();
		this.commandStack = new CommandStack();
		
		//Conteneur le plus à l'extérieur
		VBox vbox = new VBox();
		
		//Création des MenuItem et Menu
		Menu fileMenu = new Menu("File");
		Menu editMenu = new Menu("Edit");
		
		MenuItem newItem = new MenuItem("New");
		newItem.setOnAction(e -> {
			new EditorWindow(new Stage());
		});
		MenuItem closeItem = new MenuItem("Close");
		closeItem.setOnAction(e -> {
			stage.close();
			Clipboard.getInstance().removeListener(this.clipboardListener);
		});
		
		fileMenu.getItems().addAll(newItem, closeItem);
		fileMenu.getItems().add(new SeparatorMenuItem());
		
		MenuItem saveItem = new MenuItem("Save to File");
		saveItem.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			File file = fileChooser.showSaveDialog(stage);
			if(file != null) {
				stage.setTitle(this.name + file.getName());
				try {
					FileOutputStream fileStream = new FileOutputStream(file);
					ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
					objectStream.writeObject(this.board);
					objectStream.flush();
					objectStream.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		MenuItem openItem = new MenuItem("Open");
		openItem.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			File file = fileChooser.showOpenDialog(stage);
			if(file != null) {
				try {
					FileInputStream fileStream = new FileInputStream(file);
					ObjectInputStream objectStream = new ObjectInputStream(fileStream);
					
					Stage newStage = new Stage();
					new EditorWindow(newStage, (Board) objectStream.readObject());
					newStage.setTitle(this.name + file.getName());
					objectStream.close();
					
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		fileMenu.getItems().addAll(saveItem, openItem);
		
		MenuItem copyItem = new MenuItem("Copy");
		copyItem.setOnAction(e -> {
			Clipboard.getInstance().clear();
			Clipboard.getInstance().copyToClipboard(this.selection.getContents());
		});
		KeyCombination copyComb = new KeyCodeCombination(KeyCode.C, KeyCodeCombination.CONTROL_DOWN);
		copyItem.setAccelerator(copyComb);
		
		MenuItem pasteItem = new MenuItem("Paste");
		pasteItem.setOnAction(e -> {
			//TODO pasteCommand
			List<Clip> paste = Clipboard.getInstance().copyFromClipboard();
			
			this.commandStack.addCommand(new CommandAdd(this, paste));
			this.selection.clear();
			
			//les objets collés seront sélectionnés automatiquement
			this.selection.addAll(paste);
			this.draw();
		});
		KeyCombination pasteComb = new KeyCodeCombination(KeyCode.V, KeyCodeCombination.CONTROL_DOWN);
		pasteItem.setAccelerator(pasteComb);
		
		MenuItem deleteItem = new MenuItem("Delete");
		deleteItem.setOnAction(e -> {
			this.commandStack.addCommand(new CommandDelete(this, this.selection.getContents()));
			this.selection.clear();
			this.draw();
		});
		KeyCombination deleteComb = new KeyCodeCombination(KeyCode.DELETE);
		deleteItem.setAccelerator(deleteComb);
		
		editMenu.getItems().addAll(copyItem, pasteItem, deleteItem);
		editMenu.getItems().add(new SeparatorMenuItem());
		
		MenuItem groupItem = new MenuItem("Group");
		groupItem.setOnAction(e -> {
			this.commandStack.addCommand(new CommandGroup(this));
			this.draw();
		});
		KeyCombination groupComb = new KeyCodeCombination(KeyCode.G);
		groupItem.setAccelerator(groupComb);
		
		MenuItem ungroupItem = new MenuItem("Ungroup");
		ungroupItem.setOnAction(e -> {
			this.commandStack.addCommand(new CommandUngroup(this));
			this.draw();
		});
		KeyCombination ungroupComb = new KeyCodeCombination(KeyCode.G, KeyCodeCombination.SHIFT_DOWN);
		ungroupItem.setAccelerator(ungroupComb);
		
		
		editMenu.getItems().addAll(groupItem, ungroupItem);
		editMenu.getItems().add(new SeparatorMenuItem());
		
		MenuItem undoItem = new MenuItem("Undo");
		undoItem.setOnAction(e -> {
			this.commandStack.undo();
			this.draw();
		});
		KeyCombination undoComb = new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.CONTROL_DOWN);
		undoItem.setAccelerator(undoComb);
		
		MenuItem redoItem = new MenuItem("Redo");
		redoItem.setOnAction(e -> {
			this.commandStack.redo();
			this.draw();
		});
		KeyCombination redoComb = new KeyCodeCombination(KeyCode.Y, KeyCodeCombination.CONTROL_ANY);
		redoItem.setAccelerator(redoComb);
		
		editMenu.getItems().addAll(undoItem, redoItem);
		
		//Clipboard Listener (pasteItem)
		this.clipboardListener = () -> {
			Clipboard instance = Clipboard.getInstance();
			if(instance.isEmpty()) pasteItem.setDisable(true);
			else pasteItem.setDisable(false);
		};
		Clipboard.getInstance().addListener(this.clipboardListener);
		
		//Selection Listener (copyItem, deleteItem, groupItem, ungroupItem)
		this.selection.addListener(() -> {
			boolean empty = this.selection.getContents().isEmpty();
			copyItem.setDisable(empty);
			deleteItem.setDisable(empty);
			
			int selectionSize = this.selection.getContents().size();
			groupItem.setDisable(selectionSize <= 1);
			if(!empty) {
				ungroupItem.setDisable(
						!(selectionSize == 1 
						&& this.selection.hasSelectedGroup()));
			} else ungroupItem.setDisable(true);
		});
		
		//ClipboardListener (undoItem, redoItem)
		this.commandStack.setListener(() -> {
			undoItem.setDisable(this.commandStack.isUndoEmpty());
			redoItem.setDisable(this.commandStack.isRedoEmpty());
		});
		

		MenuBar menuBar = new MenuBar(fileMenu, editMenu);
		vbox.getChildren().add(menuBar);
		
		
		//Création des boutons pour sélectionner un outil
		Button boxButton = new Button("Box");
		Button ellipseButton = new Button("Ellipse");
		Button imgButton = new Button("Img");
		Button selectButton = new Button("Select");
		ColorPicker colorPicker = new ColorPicker();
		
		boxButton.setOnAction(e -> this.changeTool(this.toolRect));
		
		ellipseButton.setOnAction(e -> this.changeTool(this.toolEllipse));
		
		imgButton.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
			File selectedFile = fileChooser.showOpenDialog(stage);
			if(selectedFile != null) {
				this.changeTool(this.toolImage);
				this.toolImage.setImage(selectedFile);
			}
		});
		
		selectButton.setOnAction(e -> this.changeTool(this.toolSelection));
		
		colorPicker.getStyleClass().add("split-button");
		colorPicker.setOnAction(e -> {
			this.toolRect.setColor(colorPicker.getValue());
			this.toolEllipse.setColor(colorPicker.getValue());
			this.toolImage.setColor(colorPicker.getValue());
			
			for(Clip c : this.selection.getContents()) {
				c.setColor(colorPicker.getValue());
			}
			this.draw();
		});
		
		ToolBar toolBar = new ToolBar(boxButton, ellipseButton, imgButton, selectButton, colorPicker);		
		vbox.getChildren().add(toolBar);
		
		this.canvas = new Canvas(500, 500);
		this.canvas.getGraphicsContext2D().setFill(Color.WHITE);
		this.canvas.getGraphicsContext2D().fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
		vbox.getChildren().add(this.canvas);
		
		//écouteurs d'évenements de redimensionnement
		stage.widthProperty().addListener((obs, oldVal, newVal) -> {
			this.canvas.setWidth((double)newVal);
			this.draw();
		});
		stage.heightProperty().addListener((obs, oldVal, newVal) -> {
			this.canvas.setHeight((double)newVal - 125);
			this.draw();
		});
		
		//écouteurs des évenements de souris
		this.canvas.setOnMousePressed(e -> {
			this.currentTool.press(this, e);
			this.draw();
		});
		this.canvas.setOnMouseDragged(e -> {
			this.currentTool.drag(this, e);
			this.draw();
		});
		this.canvas.setOnMouseReleased(e -> {
			this.currentTool.release(this, e);
			this.draw();
		});
		
		vbox.getChildren().add(new Separator());
		
		//création d'indicateur de l'état de l'éditeur
		this.statusLabel = new Label("Status");
		vbox.getChildren().add(this.statusLabel);
		
		Scene scene = new Scene(vbox);
		stage.setScene(scene);
		
		this.changeTool(this.toolRect);
		stage.show();
	}
	
	public EditorWindow(Stage stage) {
		this(stage, new Board());
	}
	
	public void draw() {
		this.board.draw(this.canvas.getGraphicsContext2D());
		this.currentTool.drawFeedback(this, this.canvas.getGraphicsContext2D());
	}
	
	private void changeTool(Tool tool) {
		this.currentTool = tool;
		this.statusLabel.setText(tool.getName(this));
		}

	@Override
	public Board getBoard() {
		return this.board;
	}

	@Override
	public Selection getSelection() {
		return this.selection;
	}

	@Override
	public CommandStack getUndoStack() {
		return this.commandStack;
	}
}
