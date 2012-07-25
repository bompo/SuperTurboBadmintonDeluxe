package de.redlion.badminton.controls;

import java.io.IOException;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

import de.redlion.badminton.Birdie;
import de.redlion.badminton.Configuration;
import de.redlion.badminton.GameSession;
import de.redlion.badminton.Player;
import de.redlion.badminton.ScreenshotSaver;

public class GameControls implements InputProcessor {

	public void keyDownOptions(int keycode) {
		if (keycode == Input.Keys.F) {
			if (Gdx.app.getType() == ApplicationType.Desktop) {
				if (!org.lwjgl.opengl.Display.isFullscreen()) {
					Gdx.graphics.setDisplayMode(
							Gdx.graphics.getDesktopDisplayMode().width,
							Gdx.graphics.getDesktopDisplayMode().height, true);
					Configuration.getInstance().setFullscreen(true);
				} else {
					Gdx.graphics.setDisplayMode(800, 480, false);
					Configuration.getInstance().setFullscreen(false);
				}
			}
		}
		if (keycode == Input.Keys.F8) {
			try {
				ScreenshotSaver.saveScreenshot("screenshot");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	public void keyDownPlayer(Player player, int keycode) {
		// Player controls
		if (keycode == player.input.left) {
			if (player.state == Player.STATE.AIMING) {
				if (player.aiming == Player.AIMING.IDLE)
					player.aiming = Player.AIMING.LEFT;
				else if (player.aiming == Player.AIMING.DOWN)
					player.aiming = Player.AIMING.DOWNLEFT;
				else if (player.aiming == Player.AIMING.DOWNLEFT)
					player.aiming = Player.AIMING.DOWNLEFT;
				else if (player.aiming == Player.AIMING.DOWNRIGHT)
					player.aiming = Player.AIMING.DOWNLEFT;
				else if (player.aiming == Player.AIMING.LEFT)
					player.aiming = Player.AIMING.LEFT;
				else if (player.aiming == Player.AIMING.RIGHT)
					player.aiming = Player.AIMING.IDLE;
				else if (player.aiming == Player.AIMING.UP)
					player.aiming = Player.AIMING.UPLEFT;
				else if (player.aiming == Player.AIMING.UPLEFT)
					player.aiming = Player.AIMING.UPLEFT;
				else if (player.aiming == Player.AIMING.UPRIGHT)
					player.aiming = Player.AIMING.UPLEFT;
			} else {
				if (player.state == Player.STATE.IDLE)
					player.state = Player.STATE.LEFT;
				else if (player.state == Player.STATE.DOWN)
					player.state = Player.STATE.DOWNLEFT;
				else if (player.state == Player.STATE.DOWNLEFT)
					player.state = Player.STATE.DOWNLEFT;
				else if (player.state == Player.STATE.DOWNRIGHT)
					player.state = Player.STATE.DOWNLEFT;
				else if (player.state == Player.STATE.LEFT)
					player.state = Player.STATE.LEFT;
				else if (player.state == Player.STATE.RIGHT)
					player.state = Player.STATE.IDLE;
				else if (player.state == Player.STATE.UP)
					player.state = Player.STATE.UPLEFT;
				else if (player.state == Player.STATE.UPLEFT)
					player.state = Player.STATE.UPLEFT;
				else if (player.state == Player.STATE.UPRIGHT)
					player.state = Player.STATE.UPLEFT;
			}
		}
		if (keycode == player.input.right) {
			if (player.state == Player.STATE.AIMING) {
				if (player.aiming == Player.AIMING.IDLE)
					player.aiming = Player.AIMING.RIGHT;
				else if (player.aiming == Player.AIMING.DOWN)
					player.aiming = Player.AIMING.DOWNRIGHT;
				else if (player.aiming == Player.AIMING.DOWNLEFT)
					player.aiming = Player.AIMING.DOWNRIGHT;
				else if (player.aiming == Player.AIMING.DOWNRIGHT)
					player.aiming = Player.AIMING.DOWNRIGHT;
				else if (player.aiming == Player.AIMING.LEFT)
					player.aiming = Player.AIMING.IDLE;
				else if (player.aiming == Player.AIMING.RIGHT)
					player.aiming = Player.AIMING.RIGHT;
				else if (player.aiming == Player.AIMING.UP)
					player.aiming = Player.AIMING.UPRIGHT;
				else if (player.aiming == Player.AIMING.UPLEFT)
					player.aiming = Player.AIMING.UPRIGHT;
				else if (player.aiming == Player.AIMING.UPRIGHT)
					player.aiming = Player.AIMING.UPRIGHT;
			} else {
				if (player.state == Player.STATE.IDLE)
					player.state = Player.STATE.RIGHT;
				else if (player.state == Player.STATE.DOWN)
					player.state = Player.STATE.DOWNRIGHT;
				else if (player.state == Player.STATE.DOWNLEFT)
					player.state = Player.STATE.DOWNRIGHT;
				else if (player.state == Player.STATE.DOWNRIGHT)
					player.state = Player.STATE.DOWNRIGHT;
				else if (player.state == Player.STATE.LEFT)
					player.state = Player.STATE.IDLE;
				else if (player.state == Player.STATE.RIGHT)
					player.state = Player.STATE.RIGHT;
				else if (player.state == Player.STATE.UP)
					player.state = Player.STATE.UPRIGHT;
				else if (player.state == Player.STATE.UPLEFT)
					player.state = Player.STATE.UPRIGHT;
				else if (player.state == Player.STATE.UPRIGHT)
					player.state = Player.STATE.UPRIGHT;
			}
		}
		if (keycode == player.input.up) {
			if (player.state == Player.STATE.AIMING) {
				if (player.aiming == Player.AIMING.IDLE)
					player.aiming = Player.AIMING.UP;
				else if (player.aiming == Player.AIMING.DOWN)
					player.aiming = Player.AIMING.IDLE;
				else if (player.aiming == Player.AIMING.DOWNLEFT)
					player.aiming = Player.AIMING.UPLEFT;
				else if (player.aiming == Player.AIMING.DOWNRIGHT)
					player.aiming = Player.AIMING.UPRIGHT;
				else if (player.aiming == Player.AIMING.LEFT)
					player.aiming = Player.AIMING.UPLEFT;
				else if (player.aiming == Player.AIMING.RIGHT)
					player.aiming = Player.AIMING.UPRIGHT;
				else if (player.aiming == Player.AIMING.UP)
					player.aiming = Player.AIMING.UP;
				else if (player.aiming == Player.AIMING.UPLEFT)
					player.aiming = Player.AIMING.UPLEFT;
				else if (player.aiming == Player.AIMING.UPRIGHT)
					player.aiming = Player.AIMING.UPRIGHT;
			} else {
				if (player.state == Player.STATE.IDLE)
					player.state = Player.STATE.UP;
				else if (player.state == Player.STATE.DOWN)
					player.state = Player.STATE.IDLE;
				else if (player.state == Player.STATE.DOWNLEFT)
					player.state = Player.STATE.UPLEFT;
				else if (player.state == Player.STATE.DOWNRIGHT)
					player.state = Player.STATE.UPRIGHT;
				else if (player.state == Player.STATE.LEFT)
					player.state = Player.STATE.UPLEFT;
				else if (player.state == Player.STATE.RIGHT)
					player.state = Player.STATE.UPRIGHT;
				else if (player.state == Player.STATE.UP)
					player.state = Player.STATE.UP;
				else if (player.state == Player.STATE.UPLEFT)
					player.state = Player.STATE.UPLEFT;
				else if (player.state == Player.STATE.UPRIGHT)
					player.state = Player.STATE.UPRIGHT;
			}
		}
		if (keycode == player.input.down) {
			if (player.state == Player.STATE.AIMING) {
				if (player.aiming == Player.AIMING.IDLE)
					player.aiming = Player.AIMING.DOWN;
				else if (player.aiming == Player.AIMING.DOWN)
					player.aiming = Player.AIMING.DOWN;
				else if (player.aiming == Player.AIMING.DOWNLEFT)
					player.aiming = Player.AIMING.DOWNLEFT;
				else if (player.aiming == Player.AIMING.DOWNRIGHT)
					player.aiming = Player.AIMING.DOWNRIGHT;
				else if (player.aiming == Player.AIMING.LEFT)
					player.aiming = Player.AIMING.DOWNLEFT;
				else if (player.aiming == Player.AIMING.RIGHT)
					player.aiming = Player.AIMING.DOWNRIGHT;
				else if (player.aiming == Player.AIMING.UP)
					player.aiming = Player.AIMING.IDLE;
				else if (player.aiming == Player.AIMING.UPLEFT)
					player.aiming = Player.AIMING.DOWNLEFT;
				else if (player.aiming == Player.AIMING.UPRIGHT)
					player.aiming = Player.AIMING.DOWNRIGHT;
			} else {
				if (player.state == Player.STATE.IDLE)
					player.state = Player.STATE.DOWN;
				else if (player.state == Player.STATE.DOWN)
					player.state = Player.STATE.DOWN;
				else if (player.state == Player.STATE.DOWNLEFT)
					player.state = Player.STATE.DOWNLEFT;
				else if (player.state == Player.STATE.DOWNRIGHT)
					player.state = Player.STATE.DOWNRIGHT;
				else if (player.state == Player.STATE.LEFT)
					player.state = Player.STATE.DOWNLEFT;
				else if (player.state == Player.STATE.RIGHT)
					player.state = Player.STATE.DOWNRIGHT;
				else if (player.state == Player.STATE.UP)
					player.state = Player.STATE.IDLE;
				else if (player.state == Player.STATE.UPLEFT)
					player.state = Player.STATE.DOWNLEFT;
				else if (player.state == Player.STATE.UPRIGHT)
					player.state = Player.STATE.DOWNRIGHT;
			}
		}
		if (keycode == player.input.shoot) {
			if(GameSession.getInstance().birdie.state != Birdie.STATE.HELD && player.position.dst(GameSession.getInstance().birdie.currentPosition) >= 1.3f) {
				if (player.state != Player.STATE.AIMING) {
					if (player.state == Player.STATE.UP) {
						player.aiming = Player.AIMING.UP;
					} else if (player.state == Player.STATE.DOWN) {
						player.aiming = Player.AIMING.DOWN;
					}else if (player.state == Player.STATE.LEFT) {
						player.aiming = Player.AIMING.LEFT;
					}else if (player.state == Player.STATE.RIGHT) {
						player.aiming = Player.AIMING.RIGHT;
					}else if (player.state == Player.STATE.UPLEFT) {
						player.aiming = Player.AIMING.UPLEFT;
					}else if (player.state == Player.STATE.UPRIGHT) {
						player.aiming = Player.AIMING.UPRIGHT;
					}else if (player.state == Player.STATE.DOWNLEFT) {
						player.aiming = Player.AIMING.DOWNLEFT;
					}else if (player.state == Player.STATE.DOWNRIGHT) {
						player.aiming = Player.AIMING.DOWNRIGHT;
					}
				}
				player.state = Player.STATE.AIMING;
			}
			
//			if(player.position.dst(birdie.currentPosition) < 1.3f && player.state == Player.STATE.AIMING){
//				collisionTest(false);
//			}
			if(GameSession.getInstance().birdie.state == Birdie.STATE.HELD) {
				GameSession.getInstance().birdie.hit(player, false);
				GameSession.getInstance().birdie.state = Birdie.STATE.HIT;
				int tmp = player.aiming.ordinal();
				player.aimTime = 1;
				player.aiming = Player.AIMING.IDLE;
				player.state = Player.STATE.IDLE;
				
				player.state = Player.STATE.values()[tmp];
			}
//			else if(player.position.dst(birdie.currentPosition) > 1.3f && player.aimTime >= 1.1f) {
//				
//				player.state = Player.STATE.IDLE;
//				player.aimTime = 1;
//				int tmp = player.aiming.ordinal();
//				player.state = Player.STATE.values()[tmp];
//				player.aiming = Player.AIMING.IDLE;
//				
//			}
				
		}
		if (keycode == player.input.smash) {
			if(GameSession.getInstance().birdie.state != Birdie.STATE.HELD && player.position.dst(GameSession.getInstance().birdie.currentPosition) >= 1.3f) {
				if (player.state != Player.STATE.AIMING) {
					if (player.state == Player.STATE.UP) {
						player.aiming = Player.AIMING.UP;
					} else if (player.state == Player.STATE.DOWN) {
						player.aiming = Player.AIMING.DOWN;
					}else if (player.state == Player.STATE.LEFT) {
						player.aiming = Player.AIMING.LEFT;
					}else if (player.state == Player.STATE.RIGHT) {
						player.aiming = Player.AIMING.RIGHT;
					}else if (player.state == Player.STATE.UPLEFT) {
						player.aiming = Player.AIMING.UPLEFT;
					}else if (player.state == Player.STATE.UPRIGHT) {
						player.aiming = Player.AIMING.UPRIGHT;
					}else if (player.state == Player.STATE.DOWNLEFT) {
						player.aiming = Player.AIMING.DOWNLEFT;
					}else if (player.state == Player.STATE.DOWNRIGHT) {
						player.aiming = Player.AIMING.DOWNRIGHT;
					}
				}
				player.state = Player.STATE.AIMING;
			}
			
//			if(player.position.dst(birdie.currentPosition) < 1.3f && player.state == Player.STATE.AIMING){
//				collisionTest(true);
//			}
//			else if(player.position.dst(birdie.currentPosition) < 1.3f && player.state != Player.STATE.AIMING) {
//				birdie.hit(player, true);
//				birdie.state = Birdie.STATE.HIT;
//				player.state = Player.STATE.IDLE;
//			}
//			else if(player.position.dst(birdie.currentPosition) > 1.3f && player.aimTime >= 1.1f) {
//				
//				player.state = Player.STATE.IDLE;
//				player.aimTime = 1;
//				int tmp = player.aiming.ordinal();
//				player.state = Player.STATE.values()[tmp];
//				player.aiming = Player.AIMING.IDLE;
//				
//			}
		}
//		if (keycode == player.input.downPACE) {
//			if (!player.jump)
//				player.jump();
//		}
		if (keycode == Input.Keys.R) {
			GameSession.getInstance().birdie.currentPosition = new Vector3(2, 5, -0.5f);
		}
		
		player.moveTime = 0.0f;
	}

	public void keyUpPlayer(Player player, int keycode) {
		player.lastDirection = player.direction.cpy();
		
		if (player.state == Player.STATE.AIMING) {
			if (keycode == player.input.up) {
				if (Gdx.input.isKeyPressed(player.input.right)) {
					if (Gdx.input.isKeyPressed(player.input.up))
						player.aiming = Player.AIMING.UPRIGHT;
					else if (Gdx.input.isKeyPressed(player.input.down))
						player.aiming = Player.AIMING.DOWNRIGHT;
					else
						player.aiming = Player.AIMING.RIGHT;
				} else if (player.aiming == Player.AIMING.DOWN)
					player.aiming = Player.AIMING.DOWN;
				else if (player.aiming == Player.AIMING.DOWNLEFT) {
					if (Gdx.input.isKeyPressed(player.input.right))
						player.aiming = Player.AIMING.DOWNRIGHT;
					else
						player.aiming = Player.AIMING.DOWN;
				} else if (player.aiming == Player.AIMING.DOWNRIGHT)
					player.aiming = Player.AIMING.DOWNRIGHT;
				else if (player.aiming == Player.AIMING.LEFT)
					player.aiming = Player.AIMING.IDLE;
				else if (player.aiming == Player.AIMING.RIGHT)
					player.aiming = Player.AIMING.RIGHT;
				else if (player.aiming == Player.AIMING.UP)
					player.aiming = Player.AIMING.UP;
				else if (player.aiming == Player.AIMING.UPLEFT) {
					if (Gdx.input.isKeyPressed(player.input.right))
						player.aiming = Player.AIMING.UPRIGHT;
					else
						player.aiming = Player.AIMING.UP;
				} else if (player.aiming == Player.AIMING.UPRIGHT)
					player.aiming = Player.AIMING.UPRIGHT;
			}
			if (keycode == player.input.right) {
				if (Gdx.input.isKeyPressed(player.input.left)) {
					if (Gdx.input.isKeyPressed(player.input.up))
						player.aiming = Player.AIMING.UPLEFT;
					else if (Gdx.input.isKeyPressed(player.input.down))
						player.aiming = Player.AIMING.DOWNLEFT;
					else
						player.aiming = Player.AIMING.LEFT;
				}
				if (player.aiming == Player.AIMING.DOWN)
					player.aiming = Player.AIMING.DOWN;
				else if (player.aiming == Player.AIMING.DOWNRIGHT) {
					if (Gdx.input.isKeyPressed(player.input.left))
						player.aiming = Player.AIMING.DOWNLEFT;
					else
						player.aiming = Player.AIMING.DOWN;
				} else if (player.aiming == Player.AIMING.DOWNLEFT)
					player.aiming = Player.AIMING.DOWNLEFT;
				else if (player.aiming == Player.AIMING.RIGHT)
					player.aiming = Player.AIMING.IDLE;
				else if (player.aiming == Player.AIMING.LEFT)
					player.aiming = Player.AIMING.LEFT;
				else if (player.aiming == Player.AIMING.UP)
					player.aiming = Player.AIMING.UP;
				else if (player.aiming == Player.AIMING.UPRIGHT) {
					if (Gdx.input.isKeyPressed(player.input.left))
						player.aiming = Player.AIMING.UPLEFT;
					else
						player.aiming = Player.AIMING.UP;
				} else if (player.aiming == Player.AIMING.UPLEFT)
					player.aiming = Player.AIMING.UPLEFT;
			}
			if (keycode == player.input.up) {
				if (Gdx.input.isKeyPressed(player.input.down)) {
					if (Gdx.input.isKeyPressed(player.input.left))
						player.aiming = Player.AIMING.DOWNLEFT;
					else if (Gdx.input.isKeyPressed(player.input.right))
						player.aiming = Player.AIMING.DOWNRIGHT;
					else
						player.aiming = Player.AIMING.DOWN;
				}
				if (player.aiming == Player.AIMING.DOWN)
					player.aiming = Player.AIMING.DOWN;
				else if (player.aiming == Player.AIMING.DOWNLEFT)
					player.aiming = Player.AIMING.DOWNLEFT;
				else if (player.aiming == Player.AIMING.DOWNRIGHT)
					player.aiming = Player.AIMING.DOWNRIGHT;
				else if (player.aiming == Player.AIMING.LEFT)
					player.aiming = Player.AIMING.LEFT;
				else if (player.aiming == Player.AIMING.RIGHT)
					player.aiming = Player.AIMING.RIGHT;
				else if (player.aiming == Player.AIMING.UP)
					player.aiming = Player.AIMING.IDLE;
				else if (player.aiming == Player.AIMING.UPLEFT) {
					if (Gdx.input.isKeyPressed(player.input.down))
						player.aiming = Player.AIMING.DOWNLEFT;
					else
						player.aiming = Player.AIMING.LEFT;
				} else if (player.aiming == Player.AIMING.UPRIGHT) {
					if (Gdx.input.isKeyPressed(player.input.down))
						player.aiming = Player.AIMING.DOWNRIGHT;
					else
						player.aiming = Player.AIMING.RIGHT;
				}
			}
			if (keycode == player.input.down) {
				if (Gdx.input.isKeyPressed(player.input.up)) {
					if (Gdx.input.isKeyPressed(player.input.left))
						player.aiming = Player.AIMING.UPLEFT;
					else if (Gdx.input.isKeyPressed(player.input.right))
						player.aiming = Player.AIMING.UPRIGHT;
					else
						player.aiming = Player.AIMING.UP;
				}
				if (player.aiming == Player.AIMING.DOWN)
					player.aiming = Player.AIMING.IDLE;
				else if (player.aiming == Player.AIMING.UPLEFT)
					player.aiming = Player.AIMING.UPLEFT;
				else if (player.aiming == Player.AIMING.UPRIGHT)
					player.aiming = Player.AIMING.UPRIGHT;
				else if (player.aiming == Player.AIMING.LEFT)
					player.aiming = Player.AIMING.LEFT;
				else if (player.aiming == Player.AIMING.RIGHT)
					player.aiming = Player.AIMING.RIGHT;
				else if (player.aiming == Player.AIMING.UP)
					player.aiming = Player.AIMING.UP;
				else if (player.aiming == Player.AIMING.DOWNLEFT) {
					if (Gdx.input.isKeyPressed(player.input.up))
						player.aiming = Player.AIMING.UPLEFT;
					else
						player.aiming = Player.AIMING.LEFT;
				} else if (player.aiming == Player.AIMING.DOWNRIGHT) {
					if (Gdx.input.isKeyPressed(player.input.up))
						player.aiming = Player.AIMING.UPRIGHT;
					else
						player.aiming = Player.AIMING.RIGHT;
				}
			}
		} else {

			if (keycode == player.input.left) {
				if (Gdx.input.isKeyPressed(player.input.right)) {
					if (Gdx.input.isKeyPressed(player.input.up))
						player.state = Player.STATE.UPRIGHT;
					else if (Gdx.input.isKeyPressed(player.input.down))
						player.state = Player.STATE.DOWNRIGHT;
					else
						player.state = Player.STATE.RIGHT;
				} else if (player.state == Player.STATE.DOWN)
					player.state = Player.STATE.DOWN;
				else if (player.state == Player.STATE.DOWNLEFT) {
					if (Gdx.input.isKeyPressed(player.input.right))
						player.state = Player.STATE.DOWNRIGHT;
					else
						player.state = Player.STATE.DOWN;
				} else if (player.state == Player.STATE.DOWNRIGHT)
					player.state = Player.STATE.DOWNRIGHT;
				else if (player.state == Player.STATE.LEFT)
					player.state = Player.STATE.IDLE;
				else if (player.state == Player.STATE.RIGHT)
					player.state = Player.STATE.RIGHT;
				else if (player.state == Player.STATE.UP)
					player.state = Player.STATE.UP;
				else if (player.state == Player.STATE.UPLEFT) {
					if (Gdx.input.isKeyPressed(player.input.right))
						player.state = Player.STATE.UPRIGHT;
					else
						player.state = Player.STATE.UP;
				} else if (player.state == Player.STATE.UPRIGHT)
					player.state = Player.STATE.UPRIGHT;
			}
			if (keycode == player.input.right) {
				if (Gdx.input.isKeyPressed(player.input.left)) {
					if (Gdx.input.isKeyPressed(player.input.up))
						player.state = Player.STATE.UPLEFT;
					else if (Gdx.input.isKeyPressed(player.input.down))
						player.state = Player.STATE.DOWNLEFT;
					else
						player.state = Player.STATE.LEFT;
				}
				if (player.state == Player.STATE.DOWN)
					player.state = Player.STATE.DOWN;
				else if (player.state == Player.STATE.DOWNRIGHT) {
					if (Gdx.input.isKeyPressed(player.input.left))
						player.state = Player.STATE.DOWNLEFT;
					else
						player.state = Player.STATE.DOWN;
				} else if (player.state == Player.STATE.DOWNLEFT)
					player.state = Player.STATE.DOWNLEFT;
				else if (player.state == Player.STATE.RIGHT)
					player.state = Player.STATE.IDLE;
				else if (player.state == Player.STATE.LEFT)
					player.state = Player.STATE.LEFT;
				else if (player.state == Player.STATE.UP)
					player.state = Player.STATE.UP;
				else if (player.state == Player.STATE.UPRIGHT) {
					if (Gdx.input.isKeyPressed(player.input.left))
						player.state = Player.STATE.UPLEFT;
					else
						player.state = Player.STATE.UP;
				} else if (player.state == Player.STATE.UPLEFT)
					player.state = Player.STATE.UPLEFT;
			}
			if (keycode == player.input.up) {
				if (Gdx.input.isKeyPressed(player.input.down)) {
					if (Gdx.input.isKeyPressed(player.input.left))
						player.state = Player.STATE.DOWNLEFT;
					else if (Gdx.input.isKeyPressed(player.input.right))
						player.state = Player.STATE.DOWNRIGHT;
					else
						player.state = Player.STATE.DOWN;
				}
				if (player.state == Player.STATE.DOWN)
					player.state = Player.STATE.DOWN;
				else if (player.state == Player.STATE.DOWNLEFT)
					player.state = Player.STATE.DOWNLEFT;
				else if (player.state == Player.STATE.DOWNRIGHT)
					player.state = Player.STATE.DOWNRIGHT;
				else if (player.state == Player.STATE.LEFT)
					player.state = Player.STATE.LEFT;
				else if (player.state == Player.STATE.RIGHT)
					player.state = Player.STATE.RIGHT;
				else if (player.state == Player.STATE.UP)
					player.state = Player.STATE.IDLE;
				else if (player.state == Player.STATE.UPLEFT) {
					if (Gdx.input.isKeyPressed(player.input.down))
						player.state = Player.STATE.DOWNLEFT;
					else
						player.state = Player.STATE.LEFT;
				} else if (player.state == Player.STATE.UPRIGHT) {
					if (Gdx.input.isKeyPressed(player.input.down))
						player.state = Player.STATE.DOWNRIGHT;
					else
						player.state = Player.STATE.RIGHT;
				}
			}
			if (keycode == player.input.down) {
				if (Gdx.input.isKeyPressed(player.input.up)) {
					if (Gdx.input.isKeyPressed(player.input.left))
						player.state = Player.STATE.UPLEFT;
					else if (Gdx.input.isKeyPressed(player.input.right))
						player.state = Player.STATE.UPRIGHT;
					else
						player.state = Player.STATE.UP;
				}
				if (player.state == Player.STATE.DOWN)
					player.state = Player.STATE.IDLE;
				else if (player.state == Player.STATE.UPLEFT)
					player.state = Player.STATE.UPLEFT;
				else if (player.state == Player.STATE.UPRIGHT)
					player.state = Player.STATE.UPRIGHT;
				else if (player.state == Player.STATE.LEFT)
					player.state = Player.STATE.LEFT;
				else if (player.state == Player.STATE.RIGHT)
					player.state = Player.STATE.RIGHT;
				else if (player.state == Player.STATE.UP)
					player.state = Player.STATE.UP;
				else if (player.state == Player.STATE.DOWNLEFT) {
					if (Gdx.input.isKeyPressed(player.input.up))
						player.state = Player.STATE.UPLEFT;
					else
						player.state = Player.STATE.LEFT;
				} else if (player.state == Player.STATE.DOWNRIGHT) {
					if (Gdx.input.isKeyPressed(player.input.up))
						player.state = Player.STATE.UPRIGHT;
					else
						player.state = Player.STATE.RIGHT;
				}
			}
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}


	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		x = (int) (x / (float) Gdx.graphics.getWidth() * 800);
		y = (int) (y / (float) Gdx.graphics.getHeight() * 480);
		return false;
	}

	protected int lastTouchX;
	protected int lastTouchY;

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		x = (int) (x / (float) Gdx.graphics.getWidth() * 800);
		y = (int) (y / (float) Gdx.graphics.getHeight() * 480);
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		x = (int) (x / (float) Gdx.graphics.getWidth() * 800);
		y = (int) (y / (float) Gdx.graphics.getHeight() * 480);
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean touchMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
