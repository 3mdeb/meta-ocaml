DESCRIPTION = "Full standard library replacement for OCaml"
SECTION = "devel"
LICENSE = "Apache-2.0"

SRC_URI_append = " \
    file://0001-build-gen-exe-bytecode-only.patch \
    "

inherit opam_install


#DEPENDS += " ocaml-native opam-dune"
DEPENDS += " opam-dune"
#DEPENDS_remove = "ocaml-cross-${TARGET_ARCH}"


do_patch () {
    # copy patch to repo'
    install -d -m 755 ${OPAM_ROOT}/repo/default/packages/base/base.${PV}/files
    install -m 644 ${WORKDIR}/0001-build-gen-exe-bytecode-only.patch \
        ${OPAM_ROOT}/repo/default/packages/base/base.${PV}/files

    # ignore patch errors that might occur because we may attempt to patch the
    # same file twice as this recipe runs multiple times
    #cd ${OPAM_ROOT}/repo/default/packages/lwt/lwt.${PV}
    #patch -p1 -f < ${WORKDIR}/0001-replace-discover-exe.patch || true

    # after patching the repo, we need to remove the state cache - it will get
    # rebuild the next time opam runs
    #rm ${OPAM_ROOT}/repo/state.cache
}


OPAM_VERBOSE = "1"
