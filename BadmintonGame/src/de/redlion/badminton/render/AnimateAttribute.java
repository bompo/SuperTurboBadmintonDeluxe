/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.redlion.badminton.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g3d.materials.MaterialAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Pool;

public class AnimateAttribute extends MaterialAttribute {

	static float time = 0;
	
	protected AnimateAttribute () {
	}

	/** Creates a {@link MaterialAttribute} that is a pure {@link Color}.
	 * 
	 * @param name The name of the uniform in the {@link ShaderProgram} that will have its value set to this color. (A 'name' does
	 *           not matter for a game that uses {@link GL10}). */
	public AnimateAttribute (String name) {
		super(name);
	}

	@Override
	public void bind () {
		if (Gdx.gl10 == null) throw new RuntimeException("Can't call AnimateAttribute.bind() in a GL20 context");
		
	}

	@Override
	public void bind (ShaderProgram program) {
		time +=  Gdx.graphics.getDeltaTime() * 5.f;
		program.setUniformf("time", time);
	}

	@Override
	public MaterialAttribute copy () {
		return new AnimateAttribute(name);
	}

	@Override
	public void set (MaterialAttribute attr) {
		AnimateAttribute colAttr = (AnimateAttribute)attr;
		name = colAttr.name;
	}

	private final static Pool<AnimateAttribute> pool = new Pool<AnimateAttribute>() {
		@Override
		protected AnimateAttribute newObject () {
			return new AnimateAttribute();
		}
	};

	@Override
	public MaterialAttribute pooledCopy () {
		AnimateAttribute attr = pool.obtain();
		attr.set(this);
		return attr;
	}

	@Override
	public void free () {
		if (isPooled) pool.free(this);
	}
}
