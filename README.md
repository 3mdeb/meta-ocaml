# meta-ocaml
A Yocto Software Layer to support the OCaml Language

__(08/2018) This Repo is still very much 'Work In Progress'!__

_Note:_
This repo was started to support the [meta-vyos](https://gitlab.com/wtolkien/meta-vyos) 
project, which requires OCaml starting with release 1.2

I'm not an OCaml developer, so expect problems. Any help is very much appreciated!

I'm intentionally focussing on OCaml 4.03.0 since that version is used with VyOS, however
the resulting code should be portable enough to upgrade to newer versions later. 

## Status:

* ocaml-native produces working native compiler (for host system)
* ocaml-cross produces working cross compiler (tried for ARM)

* lots of work to do to compile libraries and actually OCaml applications

# Build Instructions:

Development is currently using OpenEmbedded/Yocto, release 'sumo'

If you are not familiar with OpenEmbedded (or the 'Yocto' derivative), extensive documentation can be found here:

https://www.yoctoproject.org/documentation

* make sure you meet the required prerequisites as described here:

https://www.yoctoproject.org/docs/current/ref-manual/ref-manual.html#ref-manual-system-requirements


* get the Yocto core packages, plus this `meta-ocaml` layer:
```
git clone -b sumo git://git.yoctoproject.org/poky.git yocto-sumo
cd yocto-sumo
git clone https://gitlab.com/wtolkien/meta-ocaml
```

* from the `yocto-sumo` directory, initialize the build-environment:
```
source oe-init-build-env
```

* if you want to play it safe, just try and build a 'vanila' Yocto image without
  OCaml to make sure everything else works. I suggest building an ARM image for the
  'qemuarm' target to get some cross-compile action going:

  Uncomment the following line in ```conf/local.conf```:
```
MACHINE ?= "qemuarm"
```
  Build a minimal image (this can take an hour during first build):
```
bitbake core-image-minimal
```
  Run the new created image using QEmu:
```
runqemu
```

* now edit the Yocto layer configuration file ```conf/bblayers.conf``` to include
  the `meta-ocaml` layer.
  Your `BBLAYERS` variable should look as below, however you may have to adjust
  your path
```
BBLAYERS ?= " \
  [your existing path]/yocto-sumo/meta \
  [your existing path]/yocto-sumo/meta-poky \
  [your existing path]/yocto-sumo/meta-yocto-bsp \
  \
  ${TOPDIR}/../meta-ocaml \
  "
```

* you can now build individual OCaml packages such as:
```
bitbake ocaml-native
bitbake ocaml-cross
[...]
```

Yocto's directory structure can be confusing to newcomers, the actual build of the two packages 
above will take place in the following directories:
```
~/sumo-test/build/tmp/work/x86_64-linux/ocaml-native
~/sumo-test/build/tmp/work/x86_64-linux/ocaml-cross
```






