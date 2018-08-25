DESCRIPTION = "ocamlbuild support for cppo, OCaml-friendly source preprocessor"
SECTION = "devel"
LICENSE = "BSD-3-Clause"

inherit opam_install

# opam packagename contains underscore(s), that's not allowed in Yocto. We need a
# manual override here...
OPAM_PKGNAME_OVERRIDE="cppo_ocamlbuild"

# executables from this package run on the host system package, therefore we need
# to include ocaml-native instead of ocaml-cross
DEPENDS += " ocaml-native opam-dune opam-ocamlbuild opam-cppo"
DEPENDS_remove = "ocaml-cross-${TARGET_ARCH}"

# more debug output, anyone?
#OPAM_VERBOSE = "1"
