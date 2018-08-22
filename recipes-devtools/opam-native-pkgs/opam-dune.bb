DESCRIPTION = "Fast, portable and opinionated build system"
SECTION = "devel"
LICENSE = "LGPLv2.1"

inherit opam_install

# executables from this package run on the host system package, therefore we need
# to include ocaml-native instead of ocaml-cross
DEPENDS += " ocaml-native"
DEPENDS_remove = "ocaml-cross-${TARGET_ARCH}"

# more debug output, anyone?
#OPAM_VERBOSE = "1"
