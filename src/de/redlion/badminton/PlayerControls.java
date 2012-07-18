package de.redlion.badminton;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.math.Vector3;

public class PlayerControls implements InputProcessor {
	
	public Player player;
	
	public PlayerControls(Player player) {
		this.player = player;
	}
	
	
	@Override
	public boolean keyDown(int keycode) {
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

		// Player controls
		if (keycode == Input.Keys.A) {
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
		if (keycode == Input.Keys.D) {
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
		if (keycode == Input.Keys.W) {
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
		if (keycode == Input.Keys.S) {
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
		if (keycode == Input.Keys.CONTROL_LEFT) {
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
		if (keycode == Input.Keys.SHIFT_LEFT) {
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
//		if (keycode == Input.Keys.SPACE) {
//			if (!player.jump)
//				player.jump();
//		}
		if (keycode == Input.Keys.R) {
			GameSession.getInstance().birdie.currentPosition = new Vector3(2, 5, -0.5f);
		}
		
		player.moveTime = 0.0f;
		
		// if (keycode == Input.Keys.S) {
		// cam.position.z += 0.1;
		// }
		// if (keycode == Input.Keys.W) {
		// cam.position.z -= 0.1;
		// }
		// if (keycode == Input.Keys.A) {
		// cam.position.x += 0.1;
		// }
		// if (keycode == Input.Keys.D) {
		// cam.position.x -= 0.1;
		// }
		// if (keycode == Input.Keys.G) {
		// cam.fieldOfView += 0.5;
		// }
		// if (keycode == Input.Keys.H) {
		// cam.fieldOfView -= 0.5;
		// }
		// if (keycode == Input.Keys.Q) {
		// cam.direction.z += 0.1;
		// }
		// if (keycode == Input.Keys.E) {
		// cam.direction.z -= 0.1;
		// }
		// if (keycode == Input.Keys.R) {
		// cam.position.y += 0.1;
		// }
		// if (keycode == Input.Keys.T) {
		// cam.position.y -= 0.1;
		// }
		// if (keycode == Input.Keys.ENTER) {
		// Gdx.app.log("pos: ", cam.position.toString());
		// Gdx.app.log("look: ", cam.direction.toString());
		// Gdx.app.log("fov: ", cam.fieldOfView + "");
		// }

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		player.lastDirection = player.direction.cpy();
		
		if (player.state == Player.STATE.AIMING) {
			if (keycode == Input.Keys.A) {
				if (Gdx.input.isKeyPressed(Input.Keys.D)) {
					if (Gdx.input.isKeyPressed(Input.Keys.W))
						player.aiming = Player.AIMING.UPRIGHT;
					else if (Gdx.input.isKeyPressed(Input.Keys.S))
						player.aiming = Player.AIMING.DOWNRIGHT;
					else
						player.aiming = Player.AIMING.RIGHT;
				} else if (player.aiming == Player.AIMING.DOWN)
					player.aiming = Player.AIMING.DOWN;
				else if (player.aiming == Player.AIMING.DOWNLEFT) {
					if (Gdx.input.isKeyPressed(Input.Keys.D))
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
					if (Gdx.input.isKeyPressed(Input.Keys.D))
						player.aiming = Player.AIMING.UPRIGHT;
					else
						player.aiming = Player.AIMING.UP;
				} else if (player.aiming == Player.AIMING.UPRIGHT)
					player.aiming = Player.AIMING.UPRIGHT;
			}
			if (keycode == Input.Keys.D) {
				if (Gdx.input.isKeyPressed(Input.Keys.A)) {
					if (Gdx.input.isKeyPressed(Input.Keys.W))
						player.aiming = Player.AIMING.UPLEFT;
					else if (Gdx.input.isKeyPressed(Input.Keys.S))
						player.aiming = Player.AIMING.DOWNLEFT;
					else
						player.aiming = Player.AIMING.LEFT;
				}
				if (player.aiming == Player.AIMING.DOWN)
					player.aiming = Player.AIMING.DOWN;
				else if (player.aiming == Player.AIMING.DOWNRIGHT) {
					if (Gdx.input.isKeyPressed(Input.Keys.A))
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
					if (Gdx.input.isKeyPressed(Input.Keys.A))
						player.aiming = Player.AIMING.UPLEFT;
					else
						player.aiming = Player.AIMING.UP;
				} else if (player.aiming == Player.AIMING.UPLEFT)
					player.aiming = Player.AIMING.UPLEFT;
			}
			if (keycode == Input.Keys.W) {
				if (Gdx.input.isKeyPressed(Input.Keys.S)) {
					if (Gdx.input.isKeyPressed(Input.Keys.A))
						player.aiming = Player.AIMING.DOWNLEFT;
					else if (Gdx.input.isKeyPressed(Input.Keys.D))
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
					if (Gdx.input.isKeyPressed(Input.Keys.S))
						player.aiming = Player.AIMING.DOWNLEFT;
					else
						player.aiming = Player.AIMING.LEFT;
				} else if (player.aiming == Player.AIMING.UPRIGHT) {
					if (Gdx.input.isKeyPressed(Input.Keys.S))
						player.aiming = Player.AIMING.DOWNRIGHT;
					else
						player.aiming = Player.AIMING.RIGHT;
				}
			}
			if (keycode == Input.Keys.S) {
				if (Gdx.input.isKeyPressed(Input.Keys.W)) {
					if (Gdx.input.isKeyPressed(Input.Keys.A))
						player.aiming = Player.AIMING.UPLEFT;
					else if (Gdx.input.isKeyPressed(Input.Keys.D))
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
					if (Gdx.input.isKeyPressed(Input.Keys.W))
						player.aiming = Player.AIMING.UPLEFT;
					else
						player.aiming = Player.AIMING.LEFT;
				} else if (player.aiming == Player.AIMING.DOWNRIGHT) {
					if (Gdx.input.isKeyPressed(Input.Keys.W))
						player.aiming = Player.AIMING.UPRIGHT;
					else
						player.aiming = Player.AIMING.RIGHT;
				}
			}
		} else {

			if (keycode == Input.Keys.A) {
				if (Gdx.input.isKeyPressed(Input.Keys.D)) {
					if (Gdx.input.isKeyPressed(Input.Keys.W))
						player.state = Player.STATE.UPRIGHT;
					else if (Gdx.input.isKeyPressed(Input.Keys.S))
						player.state = Player.STATE.DOWNRIGHT;
					else
						player.state = Player.STATE.RIGHT;
				} else if (player.state == Player.STATE.DOWN)
					player.state = Player.STATE.DOWN;
				else if (player.state == Player.STATE.DOWNLEFT) {
					if (Gdx.input.isKeyPressed(Input.Keys.D))
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
					if (Gdx.input.isKeyPressed(Input.Keys.D))
						player.state = Player.STATE.UPRIGHT;
					else
						player.state = Player.STATE.UP;
				} else if (player.state == Player.STATE.UPRIGHT)
					player.state = Player.STATE.UPRIGHT;
			}
			if (keycode == Input.Keys.D) {
				if (Gdx.input.isKeyPressed(Input.Keys.A)) {
					if (Gdx.input.isKeyPressed(Input.Keys.W))
						player.state = Player.STATE.UPLEFT;
					else if (Gdx.input.isKeyPressed(Input.Keys.S))
						player.state = Player.STATE.DOWNLEFT;
					else
						player.state = Player.STATE.LEFT;
				}
				if (player.state == Player.STATE.DOWN)
					player.state = Player.STATE.DOWN;
				else if (player.state == Player.STATE.DOWNRIGHT) {
					if (Gdx.input.isKeyPressed(Input.Keys.A))
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
					if (Gdx.input.isKeyPressed(Input.Keys.A))
						player.state = Player.STATE.UPLEFT;
					else
						player.state = Player.STATE.UP;
				} else if (player.state == Player.STATE.UPLEFT)
					player.state = Player.STATE.UPLEFT;
			}
			if (keycode == Input.Keys.W) {
				if (Gdx.input.isKeyPressed(Input.Keys.S)) {
					if (Gdx.input.isKeyPressed(Input.Keys.A))
						player.state = Player.STATE.DOWNLEFT;
					else if (Gdx.input.isKeyPressed(Input.Keys.D))
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
					if (Gdx.input.isKeyPressed(Input.Keys.S))
						player.state = Player.STATE.DOWNLEFT;
					else
						player.state = Player.STATE.LEFT;
				} else if (player.state == Player.STATE.UPRIGHT) {
					if (Gdx.input.isKeyPressed(Input.Keys.S))
						player.state = Player.STATE.DOWNRIGHT;
					else
						player.state = Player.STATE.RIGHT;
				}
			}
			if (keycode == Input.Keys.S) {
				if (Gdx.input.isKeyPressed(Input.Keys.W)) {
					if (Gdx.input.isKeyPressed(Input.Keys.A))
						player.state = Player.STATE.UPLEFT;
					else if (Gdx.input.isKeyPressed(Input.Keys.D))
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
					if (Gdx.input.isKeyPressed(Input.Keys.W))
						player.state = Player.STATE.UPLEFT;
					else
						player.state = Player.STATE.LEFT;
				} else if (player.state == Player.STATE.DOWNRIGHT) {
					if (Gdx.input.isKeyPressed(Input.Keys.W))
						player.state = Player.STATE.UPRIGHT;
					else
						player.state = Player.STATE.RIGHT;
				}
			}
		}

		// if (keycode == Input.Keys.CONTROL_LEFT) {
		// if (player.position.dst(birdie.position) < 1.3f
		// && birdie.state != Birdie.STATE.HIT) {
		// birdie.state = Birdie.STATE.HIT;
		// // IDLE, UP, DOWN, LEFT, RIGHT, DOWNLEFT, UPLEFT, DOWNRIGHT,
		// // UPRIGHT;
		// if (player.aiming == Player.AIMING.UP)
		// birdie.hit(new Vector3(0, -1, 0), false);
		// else if (player.aiming == Player.AIMING.LEFT)
		// birdie.hit(new Vector3(-1, -0.5f, 0), false);
		// else if (player.aiming == Player.AIMING.RIGHT)
		// birdie.hit(new Vector3(1, -0.5f, 0), false);
		// else if (player.aiming == Player.AIMING.UPLEFT)
		// birdie.hit(new Vector3(-1, -1, 0), false);
		// else if (player.aiming == Player.AIMING.UPRIGHT)
		// birdie.hit(new Vector3(1, -1, 0), false);
		// }
		// player.state = Player.STATE.IDLE;
		// }
		// if (keycode == Input.Keys.SHIFT_LEFT) {
		// if (player.position.dst(birdie.position) < 1.3f
		// && birdie.state != Birdie.STATE.HIT) {
		// birdie.state = Birdie.STATE.HIT;
		// birdie.hit(player.direction, true);
		// }
		// player.state = Player.STATE.IDLE;
		// }

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
