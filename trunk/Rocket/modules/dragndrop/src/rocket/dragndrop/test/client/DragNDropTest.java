/*
 * Copyright Miroslav Pokorny
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rocket.dragndrop.test.client;

import rocket.dragndrop.client.DragMoveEvent;
import rocket.dragndrop.client.DragNDropListener;
import rocket.dragndrop.client.DragStartEvent;
import rocket.dragndrop.client.DraggablePanel;
import rocket.dragndrop.client.DropEvent;
import rocket.dragndrop.client.DropTargetPanel;
import rocket.style.client.Css;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.util.client.StackTrace;
import rocket.widget.client.Html;
import rocket.widget.client.Image;
import rocket.widget.client.Label;
import rocket.widget.client.Widgets;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

public class DragNDropTest implements EntryPoint {

	final int SWAP_TIMEOUT = 2500;

	Counter dragStarts;

	Counter dragMoves;

	Counter drops;

	CheckBox disableDragStarts;

	CheckBox disableDragMoves;

	CheckBox disableDrops;

	public void onModuleLoad() {

		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();

				Window.alert(StackTrace.asString(caught));
			}
		});

		final RootPanel rootPanel = RootPanel.get( "main");

		this.dragStarts = new Counter("Drag starts: ");
		rootPanel.add(dragStarts);

		this.dragMoves = new Counter("Drag moves: ");
		rootPanel.add(dragMoves);

		this.drops = new Counter("Drops: ");
		rootPanel.add(drops);

		this.disableDragStarts = new CheckBox();
		disableDragStarts.setChecked(false);
		rootPanel.add(new Label("Disable drag starts"));
		rootPanel.add(disableDragStarts);

		this.disableDragMoves = new CheckBox();
		disableDragMoves.setChecked(true);
		rootPanel.add(new Label("Disable drag too many moves"));
		rootPanel.add(disableDragMoves);

		this.disableDrops = new CheckBox();
		disableDrops.setChecked(false);
		rootPanel.add(new Label("Disable drops"));
		rootPanel.add(disableDrops);

		rootPanel.add(new Label("Grid"));
		rootPanel.add(createGrid());

		rootPanel.add(new Label("FlowPanel"));
		rootPanel.add(this.createFlowPanel());

		rootPanel.add(new Label("Bin"));
		rootPanel.add(createBin());

		rootPanel.getElement().scrollIntoView();
	}

	Widget createGrid() {

		final Grid grid = new Grid(3, 3);
		grid.setStyleName("rocket-dragNDropTest-grid");
		grid.setBorderWidth(0);
		grid.setCellPadding(1);
		grid.setCellSpacing(1);
		grid.setWidget(0, 0, new Image("anchor.png"));
		grid.setWidget(1, 1, new Image("bell.png"));
		grid.setWidget(2, 2, new Image("bomb.png"));

		final DraggablePanel draggablePanel = new DraggablePanel();
		draggablePanel.addDragNDropListener(new DragNDropListenerTemplate() {
			void onDragStart0(final DragStartEvent event) {
				final Element target = event.getDraggedElement();

				// figure out which cell widget is being dragged.
				final Widget cellWidget = Widgets.findWidget(target, grid.iterator());
				event.setWidget(cellWidget);

				// find the cell and grab its innerHTML.
				final Element cellElement = DragNDropTest.this.findCell(grid, target);
				event.setDragged(new Html(DOM.getInnerHTML(cellElement)));

				event.setXOffset(-cellWidget.getOffsetWidth() / 2);
				event.setYOffset(-cellWidget.getOffsetHeight() / 2);
			}

			void onDragMove0(final DragMoveEvent event) {

			}

			void onDrop0(final DropEvent event) {
			}
		});
		draggablePanel.setWidget(grid);

		final DropTargetPanel dropTarget = new DropTargetPanel() {
			public void accept(final DropEvent event) {
				setCellWidget(grid, event.getDroppedOverElement(), event.getWidget());
			}
		};
		dropTarget.setWidget(draggablePanel);

		return dropTarget;
	}

	void setCellWidget(final Grid grid, final Element element, final Widget widget) {
		final int rows = grid.getRowCount();
		final int columns = grid.getColumnCount();
		final CellFormatter cellFormatter = grid.getCellFormatter();

		BothForLoops: for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				final Element cell = cellFormatter.getElement(r, c);
				if (DOM.isOrHasChild(cell, element)) {
					grid.setWidget(r, c, widget);
					break BothForLoops;
				}
			}
		}
	}

	Element findCell(final Grid grid, final Element element) {
		Element found = null;

		final int rows = grid.getRowCount();
		final int columns = grid.getColumnCount();
		final CellFormatter cellFormatter = grid.getCellFormatter();

		BothForLoops: for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				final Element cell = cellFormatter.getElement(r, c);
				if (DOM.isOrHasChild(cell, element)) {
					found = cell;
					break BothForLoops;
				}
			}
		}

		return found;
	}

	Widget createBin() {
		final DropTargetPanel dropTarget = new DropTargetPanel() {
			public void accept(final DropEvent dropEvent) {
				final Widget widget = dropEvent.getWidget();
				widget.removeFromParent();
			}
		};

		dropTarget.setWidget(new Image("bin_empty.png"));
		return dropTarget;
	}

	Widget createFlowPanel() {
		final FlowPanel horizontalPanel = new FlowPanel();
		horizontalPanel.addStyleName("rocket-dragNDropTest-flowPanel");
		horizontalPanel.add(new Image("hourglass.png"));
		horizontalPanel.add(new Html("<div style='width: 100%'></div>"));

		final DropTargetPanel dropTargetPanel = new DropTargetPanel() {
			public void accept(final DropEvent dropEvent) {
				final Widget widget = dropEvent.getWidget();
				widget.removeFromParent();
				horizontalPanel.add(widget);
			}
		};
		final DraggablePanel draggablePanel = new DraggablePanel();
		draggablePanel.add(horizontalPanel);

		dropTargetPanel.setWidget(draggablePanel);

		draggablePanel.addDragNDropListener(new DragNDropListenerTemplate() {
			void onDragStart0(final DragStartEvent event) {
				// because a horizontal panel can have many widgets pick the
				// widget actually being dragged
				final Widget widget = Widgets.findWidget(event.getDraggedElement(), horizontalPanel.iterator());
				event.setWidget(widget);
				event.setDragged(new Html(DOM.getInnerHTML(widget.getElement())));
			}

			void onDragMove0(final DragMoveEvent event) {
			}

			void onDrop0(final DropEvent event) {
			}
		});

		return dropTargetPanel;
	}

	abstract class DragNDropListenerTemplate implements DragNDropListener {
		public void onDragStart(final DragStartEvent event) {
			dragStarts.increment();

			if (disableDragStarts.isChecked()) {
				event.stop();
			} else {
				this.onDragStart0(event);

				final long now = System.currentTimeMillis();
				this.timestamp = now;
				this.swapTimeout = now + SWAP_TIMEOUT;
			}
		}

		long timestamp = 0;

		long swapTimeout = 0;

		Widget otherWidget = new Image("stop.png");

		abstract void onDragStart0(DragStartEvent event);

		public void onDragMove(final DragMoveEvent event) {
			dragMoves.increment();

			final long now = System.currentTimeMillis();

			if (now >= this.swapTimeout) {
				this.timestamp = SWAP_TIMEOUT;
				this.swapTimeout = now + SWAP_TIMEOUT;

				final Widget current = event.getDragged();
				event.setDragged(this.otherWidget);
				this.otherWidget = current;

				System.out.println("Swapping: ");
			}
			final double opacity = 1.0f * (this.swapTimeout - now) / SWAP_TIMEOUT;
			InlineStyle.setDouble(event.getDragged().getElement(), Css.OPACITY, opacity, CssUnit.NONE);

			if (this.otherWidget == event.getDragged()) {
				event.stop();
			}

			this.onDragMove0(event);
		}

		abstract void onDragMove0(DragMoveEvent event);

		public void onDrop(final DropEvent event) {
			drops.increment();

			this.onDrop0(event);
		}

		abstract void onDrop0(DropEvent event);
	}

	static class Counter extends Label {

		Counter(final String prefix) {
			this.prefix = prefix;

			this.setText(prefix + "?");
		}

		String prefix = null;

		int counter = 0;

		void increment() {
			this.counter++;
			this.setText(prefix + counter);
		}
	}
}