package it.unipd.dei.db.kayak.league_manager;

import it.unipd.dei.db.kayak.league_manager.data.Club;

import java.io.File;
import java.util.Collection;

import com.vaadin.data.Container;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

// table for showing clubs, searchable by club name
public class ClubTable extends Table {
	private StringPropertyFilter nameFilter;

	public ClubTable(Collection<Club> clubs) {
		super("");

		Container clubContainer = (IndexedContainer) this
				.getContainerDataSource();
		clubContainer.addContainerProperty("", Button.class, null);
		clubContainer.addContainerProperty("Name", String.class, null);
		clubContainer.addContainerProperty("Address", String.class, null);
		clubContainer.addContainerProperty("Website", String.class, null);
		clubContainer.addContainerProperty("Email", String.class, null);
		clubContainer.addContainerProperty("Phone", String.class, null);

		this.setContainerDataSource(clubContainer);

		for (Club c : clubs) {
			this.addClub(c);
		}

		Filterable filterable = (Filterable) clubContainer;
		nameFilter = new StringPropertyFilter("", "Name");
		filterable.addContainerFilter(nameFilter);

		this.setColumnExpandRatio("", 0);
		this.setColumnWidth("", 45);
		this.setColumnExpandRatio("Name", 1);
		this.setColumnExpandRatio("Address", 1);
		this.setColumnExpandRatio("Website", 1);
		this.setColumnExpandRatio("Email", 1);
		this.setColumnExpandRatio("Phone", 0);

		this.setColumnHeader("Name", "Nome");
		this.setColumnHeader("Address", "Indirizzo");
		this.setColumnHeader("Website", "Sito Web");
		this.setColumnHeader("Email", "Email");
		this.setColumnHeader("Phone", "Telefono");
	}

	public void filterClubNames(String text) {
		if (!text.toLowerCase().equals(nameFilter.getFilter().toLowerCase())) {
			Filterable container = (Filterable) this.getContainerDataSource();
			container.removeContainerFilter(nameFilter);
			nameFilter.setFilter(text);
			container.addContainerFilter(nameFilter);
		}
	}

	public void addClub(Club club) {
		final long clubID = club.getID();
		Button btn = new Button("", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Home home = ((MyVaadinUI) UI.getCurrent()).getHome();
				home.showClubDetailsSubWindow(clubID);
			}
		});

		String basepath = VaadinService.getCurrent().getBaseDirectory()
				.getAbsolutePath();
		FileResource resource = new FileResource(new File(basepath
				+ "/WEB-INF/images/magnifier.png"));
		btn.setIcon(resource);

		this.addItem(new Object[] { btn, club.getName(), club.getAddress(),
				club.getWebsite(), club.getEmail(), club.getPhone() },
				club.getID());
	}

	public void removeClub(long clubID) {
		this.removeItem(clubID);
	}

	public boolean hasClub(long clubID) {
		return this.containsId(clubID);
	}
}
