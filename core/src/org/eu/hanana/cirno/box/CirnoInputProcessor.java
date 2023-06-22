package org.eu.hanana.cirno.box;

import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;
import java.util.List;

public class CirnoInputProcessor implements InputProcessor {
    public static CirnoInputProcessor INSTANCE;
    public List<InputProcessor> inputProcessors = new ArrayList<>();
    public CirnoInputProcessor(){
        INSTANCE=this;
    }

    @Override
    public boolean keyDown(int keycode) {
        boolean r=false;
        for (InputProcessor inputProcessor : inputProcessors) {
            if(inputProcessor.keyDown(keycode)) r=true;
        }
        return r;
    }


    @Override
    public boolean keyUp(int keycode) {
        boolean r=false;
        for (InputProcessor inputProcessor : inputProcessors) {
            if(inputProcessor.keyUp(keycode)) r=true;
        }
        return r;
    }

    @Override
    public boolean keyTyped(char character) {
        boolean r=false;
        for (InputProcessor inputProcessor : inputProcessors) {
            if(inputProcessor.keyTyped(character)) r=true;
        }
        return r;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean r=false;
        for (InputProcessor inputProcessor : inputProcessors) {
            if(inputProcessor.touchDown(screenX,screenY,pointer,button)) r=true;
        }
        return r;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        boolean r=false;
        for (InputProcessor inputProcessor : inputProcessors) {
            if(inputProcessor.touchUp(screenX,screenY,pointer,button)) r=true;
        }
        return r;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        boolean r=false;
        for (InputProcessor inputProcessor : inputProcessors) {
            if(inputProcessor.touchDragged(screenX,screenY,pointer)) r=true;
        }
        return r;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        boolean r=false;
        for (InputProcessor inputProcessor : inputProcessors) {
            if(inputProcessor.mouseMoved(screenX,screenY)) r=true;
        }
        return r;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        boolean r=false;
        for (InputProcessor inputProcessor : inputProcessors) {
            if(inputProcessor.scrolled(amountX,amountY)) r=true;
        }
        return r;
    }
}
