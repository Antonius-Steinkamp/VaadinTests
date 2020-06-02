package de.anst.chat;

import java.time.format.DateTimeFormatter;

import org.vaadin.marcus.shortcut.Shortcut;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

/**
 * @author mstahv
 */
@Push
@Route("chat")
@StyleSheet("styles.css")
public class MainView extends VerticalLayout {

	private final VerticalLayout messageLayout;
	private String user;
	private Disposable registration;

	private static final UnicastProcessor<ChatMessage> messageDistributor = UnicastProcessor.create();

	private static final Flux<ChatMessage> chatMessages = messageDistributor.replay(20).autoConnect();

	public MainView() {
		setSizeFull();

		H1 header = new H1("chat");
		header.getStyle().set("color", "darkblue").set("width", "100%").set("text-align", "center");

		TextField msgField = new TextField();
		msgField.setPlaceholder("Enter your message here");
		msgField.setValueChangeMode(ValueChangeMode.EAGER);

		Dialog dialog = new Dialog();
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(false);
		TextField username = new TextField("Your name?");
		username.focus();
		Button joinBnt = new Button("Join!");
		joinBnt.setEnabled(false);
		username.setValueChangeMode(ValueChangeMode.EAGER);
		username.addValueChangeListener(event -> {
			joinBnt.setEnabled(!isBlank(event.getSource().getValue()));
		});
		dialog.add(username, joinBnt);
		dialog.open();
		joinBnt.addClickListener(e -> {
			user = username.getValue();
			if (!isBlank(user)) {
				dialog.close();
				header.setText(header.getText() + " as " + user);
				msgField.focus();
			}
		});
		Shortcut.add(username, Key.ENTER, joinBnt::click);

		messageLayout = new VerticalLayout();
		messageLayout.setSizeFull();
		expand(messageLayout);
		messageLayout.setClassName("message-layout");
		messageLayout.getStyle().set("overflow-y", "scroll");

		registration = chatMessages.subscribe(chatMsg -> {
			if (getUI().isPresent()) {
				getUI().get().access(() -> {
					addMessage(chatMsg);
				});
			} else {
				if (registration != null) {
					registration.dispose();
				}
			}
		});

		Button sendBtn = new Button("Send!");
		sendBtn.setEnabled(false);
		sendBtn.addClickListener(e -> {
			if (!isBlank(msgField.getValue())) {
				messageDistributor.onNext(new ChatMessage(user, msgField.getValue()));
				msgField.clear();
			}
		});
		Shortcut.add(msgField, Key.ENTER, sendBtn::click);
		msgField.addValueChangeListener(event -> {
			sendBtn.setEnabled(!isBlank(event.getSource().getValue()));
		});
		final HorizontalLayout form = new HorizontalLayout(msgField, sendBtn);
		form.setWidth("100%");
		form.expand(msgField);
		msgField.setSizeFull();

		add(header, messageLayout, form);
	}

	private static boolean isBlank(final String string) {
		return string == null || string.trim().isEmpty();
	}

	private void addMessage(ChatMessage chatMessage) {
		final String chatMessageText = chatMessage.getTime().format(DateTimeFormatter.ISO_DATE_TIME) + " -- " + chatMessage.getFrom() + " says: "
				+ chatMessage.getMessage();
		Paragraph paragraph = new Paragraph(chatMessageText);
		paragraph.getStyle().set("margin", "0");
		messageLayout.add(paragraph);
	}

}