DESCRIPTION = "Tools for authors of ppx rewriters and other syntactic tools"
SECTION = "devel"
LICENSE = "MIT"

inherit opam_install

# opam packagename contains underscore(s), that's not allowed in Yocto. We need a
# manual override here...
OPAM_PKGNAME_OVERRIDE = "ppx_tools"

# executables from this package run on the host system package, therefore we need
# to include ocaml-native instead of ocaml-cross
DEPENDS += " ocaml-native"
DEPENDS_remove = "ocaml-cross-${TARGET_ARCH}"


OPAM_VERBOSE = "1"
