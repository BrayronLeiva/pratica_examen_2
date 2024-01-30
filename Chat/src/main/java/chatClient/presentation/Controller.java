package chatClient.presentation;

import chatClient.logic.ServiceProxy;
import chatProtocol.Candidato;
import chatProtocol.Message;
import chatProtocol.User;

import java.util.ArrayList;

public class Controller {
    View view;
    Model model;
    
    ServiceProxy localService;
    
    public Controller(View view, Model model) throws Exception {
        this.view = view;
        this.model = model;
        localService = (ServiceProxy)ServiceProxy.instance();
        localService.setController(this);
        view.setController(this);
        view.setModel(model);
        User u = new User();
        this.login(u);
    }

    public void login(User u) throws Exception{
        User logged=ServiceProxy.instance().login(u);
        model.setCurrentUser(logged);
        model.commit(Model.USER);
    }

    public void agregarCandidato(){

        Message message = new Message();
        //message.setMessage(text);
        Candidato obj = this.generar_candidato();
        message.setSender(model.getCurrentUser());
        ServiceProxy.instance().agregar_cantidato(obj);
        model.commit(Model.CHAT);
    }

    public void validar_excepciones() {
        try {
            if (view.getTxf_id().getText().equals("") || view.getTxf_nombre().getText().equals("")) {
                throw new Exception("Campos Vacios - Por favor verifique");
            }
        }catch (Exception ex){
            view.lanzar_mensaje(ex.getMessage());
        }
    }

    public Candidato generar_candidato() {
        String id = view.getTxf_id().getText();
        String nombre = view.getTxf_nombre().getText();
        validar_excepciones();
        return new Candidato(id, nombre, 0);
    }

    public void logout(){
        try {
            ServiceProxy.instance().logout(model.getCurrentUser());
            model.setMessages(new ArrayList<>());
            model.commit(Model.CHAT);
        } catch (Exception ex) {
        }
        model.setCurrentUser(null);
        model.commit(Model.USER+Model.CHAT);
    }
        
    public void deliver(Message message){
        model.messages.add(message);
        model.commit(Model.CHAT);       
    }    
}
