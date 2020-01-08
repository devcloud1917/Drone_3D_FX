package sample;

import Drone_Point.MapGui;
import Drone_Point.dronePoint;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import javafx.scene.*;
import javafx.scene.paint.Color;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main extends Application {

    public static final int c = 50;
    public static double FRONT_DIST = 50;
    public static double LEFT_DIST = 50;
    public static double RIGHT_DIST = 50;
    private MapGui droneMap = new MapGui();
    private int droneMapIndex=0;
    private static double AXIS_LENGTH = 250.0;
    private static double LEFT_ANGEL = 90;
    private static double RIGHT_ANGEL = -90;
    private static double YAW = 0;
    private static final double CONTROL_MULTIPLIER = 0.1;    private static final double SHIFT_MULTIPLIER = 10.0;    private static final double MOUSE_SPEED = 0.1;    private static final double ROTATION_SPEED = 2.0;    private static final double TRACK_SPEED = 0.3;
    public static final int WIDTH = 2000;
    public static final int HEIGHT = 2000;
    public static final int DRONEBODYWIDTH = 20;
    public static final int DRONEBODYHEIGHT = 7;
    public static final int DRONEBODYDEPTH = 7;

    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;
    long nowFirst=0;
    final Group root = new Group();

    Xform droneComplex = new Xform();
    Xform droneBodyXform = new Xform();
    Xform frontXform = new Xform();
    Xform ledtXform = new Xform();
    Xform rightXform = new Xform();
    Xform frontCylinderXF = new Xform();
    Xform leftCylinderXF = new Xform();
    Xform rightCylinderXF = new Xform();


    final Xform world = new Xform();

    final Xform axisGroup = new Xform();

    final Xform droneGroup = new Xform();

    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform();
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();

//    Xform droneComplex = new Xform();
//    Xform droneBodyXform = new Xform();
//    Xform frontXform = new Xform();
//    Xform ledtXform = new Xform();
//    Xform rightXform = new Xform();
//    Xform frontCylinderXF = new Xform();
//    Xform leftCylinderXF = new Xform();
//    Xform rightCylinderXF = new Xform();
final PhongMaterial droneMaterial = new PhongMaterial();


    final PhongMaterial frontMaterial = new PhongMaterial();



    final PhongMaterial leftMaterial= new PhongMaterial();


    final PhongMaterial rightMaterial = new PhongMaterial();

    Box droneBody = new Box(DRONEBODYWIDTH, DRONEBODYHEIGHT, DRONEBODYDEPTH); // the drone


    //all the xform

    Sphere frontDroneSphere = new Sphere(3);


    Sphere lefttDroneSphere = new Sphere(3);


    Sphere rightDroneSphere = new Sphere(3);


    private static final double CAMERA_INITIAL_DISTANCE = -450;
    private static final double CAMERA_INITIAL_X_ANGLE = 70.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 320.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private  double t = 0;
    private AnimationTimer timer;

    @Override
    public void start(Stage primaryStage) throws InterruptedException {

        // grt the data
        try {
            droneMap.getData("C:\\Users\\pikal\\Downloads\\Drone_3D_FX\\Drone_3D_FX\\draw.xlsx");

        } catch (IOException e) {
            System.out.println("Eror reading file ");
        }

        droneMaterial.setDiffuseColor(Color.DARKGRAY);
        droneMaterial.setSpecularColor(Color.DARKGRAY);

        leftMaterial.setDiffuseColor(Color.BLUE);
        leftMaterial.setSpecularColor(Color.BLUE);

        rightMaterial.setDiffuseColor(Color.DARKRED);
        rightMaterial.setSpecularColor(Color.DARKRED);

        frontMaterial.setDiffuseColor(Color.DARKGREEN);
        frontMaterial.setSpecularColor(Color.DARKGREEN);

        droneBody.setMaterial(droneMaterial);
        frontDroneSphere.setMaterial(frontMaterial);
        lefttDroneSphere.setMaterial(leftMaterial);
        rightDroneSphere.setMaterial(rightMaterial);


        Scene scene = new Scene(root, WIDTH, HEIGHT, true);
        scene.setFill(Color.BLACK);
        handleKeyboard(scene, world);
        handleMouse(scene, world);
        primaryStage.setTitle("Simon JavaFX 3D Drone");
        buildCamera();
        buildAxes();

        buildDroneGroup();
        primaryStage.setScene(scene);


        primaryStage.show();



        scene.setCamera(camera);


         AnimationTimer timerDrone = new AnimationTimer(){
            @Override
            public void handle(long now) {
                if (now-nowFirst>40000000 & droneMapIndex<droneMap.dronePointArr.size()){

                    onUpdate();
                    nowFirst=now;
                    droneMapIndex=droneMapIndex+1;
                }

            }
        };
        timerDrone.start();


    }






    private void onUpdate() {
        dronePoint x= droneMap.dronePointArr.get(droneMapIndex);
        {

            FRONT_DIST = x.getDroneFront();
            LEFT_DIST = x.getDroneleft();
            RIGHT_DIST = x.getDroneRight();
            YAW = x.getDroneYaw();
          //  root.getChildren().add()
            droneGroup.setTranslate(x.getDroneX()*50,x.getDroneY()*50,x.getDroneZ()*50);
            droneGroup.setRotateY(x.getDroneYaw());
//            droneGroup.setTy(x.getDroneY());
//            droneGroup.setTz(x.getDroneZ());
            frontXform.setTx(FRONT_DIST/10);
            ledtXform.setTx( LEFT_DIST/10);
            rightXform.setTx(RIGHT_DIST/10);
          //  frontXform.setTranslate(x.getDroneFront(),x.getDroneleft(),x.getDroneRight());
            Sphere wayDroneSphere = new Sphere(0.3);
            wayDroneSphere.setTranslateX(x.getDroneX()*50);
            wayDroneSphere.setTranslateY(x.getDroneY()*50);
            wayDroneSphere.setTranslateZ(x.getDroneZ()*50);
//            long startTime=System.currentTimeMillis()/1000;
//            while (System.currentTimeMillis()/1000-startTime<1);
            root.getChildren().add(wayDroneSphere);
            //  root.setTranslateX(x.getDroneX());

//            Path path = new Path();
//            path.getElements().add(new MoveTo(200,100));
//            PathTransition pathTransition = new PathTransition();
//            pathTransition.setDuration(Duration.millis(4000));
//            pathTransition.setPath(path);
//            pathTransition.setNode(moleculeGroup);
//            pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
//            pathTransition.setCycleCount(Timeline.INDEFINITE);
//            pathTransition.setAutoReverse(true);
//            pathTransition.play();
//            if(root.getChildren().isEmpty()) continue;
//            root.getChildren().remove(moleculeGroup);
//
//            frontXform.setTx(FRONT_DIST);
//            ledtXform.setTx(LEFT_DIST);
//            rightXform.setTx(RIGHT_DIST);
//            frontCylinderXF.setTx(FRONT_DIST/2);
//            leftCylinderXF.setTx(LEFT_DIST/2);
//            rightCylinderXF.setTx(RIGHT_DIST/2);
//
//            root.getChildren().removeAll();
//            moleculeGroup.getChildren().remove(droneComplex);
//            moleculeGroup.getChildren().add(droneComplex);
//            root.getChildren().addAll(moleculeGroup);


        }

    }

    private void handleKeyboard(Scene scene, final Node root) {

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case Z:
                        cameraXform2.t.setX(0.0);
                        cameraXform2.t.setY(0.0);
                        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
                        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
                        break;
                    case X:
                        axisGroup.setVisible(!axisGroup.isVisible());
                        break;
                    case V:
                        droneGroup.setVisible(!droneGroup.isVisible());
                        break;
                } // switch
            } // handle()
        });  // setOnKeyPressed
    }  //  handleKeyboard()

    private void handleMouse(Scene scene, final Node root) {

        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX);
                mouseDeltaY = (mousePosY - mouseOldY);

                double modifier = 1.0;

                if (me.isControlDown()) {
                    modifier = CONTROL_MULTIPLIER;
                }
                if (me.isShiftDown()) {
                    modifier = SHIFT_MULTIPLIER;
                }
                if (me.isPrimaryButtonDown()) {
                    cameraXform.ry.setAngle(cameraXform.ry.getAngle() -
                            mouseDeltaX*modifier*ROTATION_SPEED);  //
                    cameraXform.rx.setAngle(cameraXform.rx.getAngle() +
                            mouseDeltaY*modifier*ROTATION_SPEED);  // -
                }
                else if (me.isSecondaryButtonDown()) {
                    double z = camera.getTranslateZ();
                    double newZ = z + mouseDeltaX*MOUSE_SPEED*modifier;
                    camera.setTranslateZ(newZ);
                }
                else if (me.isMiddleButtonDown()) {
                    cameraXform2.t.setX(cameraXform2.t.getX() +
                            mouseDeltaX*MOUSE_SPEED*modifier*TRACK_SPEED);  // -
                    cameraXform2.t.setY(cameraXform2.t.getY() +
                            mouseDeltaY*MOUSE_SPEED*modifier*TRACK_SPEED);  // -
                }
            }
        }); // setOnMouseDragged
    } //handleMouse

    private void buildCamera() {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        cameraXform3.setRotateZ(180.0);

        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);

    }

    private void buildAxes() {
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        final Box xAxis = new Box(AXIS_LENGTH, 1, 1);
        final Box yAxis = new Box(1, AXIS_LENGTH, 1);
        final Box zAxis = new Box(1, 1, AXIS_LENGTH);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        axisGroup.setVisible(true);
        root.getChildren().addAll(axisGroup);
    }

    private void buildDroneGroup() {









//        Cylinder frontbond1Cylinder = new Cylinder(0.5, FRONT_DIST);
//        frontbond1Cylinder.setMaterial(frontMaterial);
//        frontCylinderXF.getChildren().add(frontbond1Cylinder);
//
//
//        Cylinder leftbondCylinder = new Cylinder(0.5, LEFT_DIST);
//        leftbondCylinder.setMaterial(leftMaterial);
//        leftCylinderXF.getChildren().add(leftbondCylinder);
//
//
//        Cylinder rightbondCylinder = new Cylinder(0.5, RIGHT_DIST);
//        rightbondCylinder.setMaterial(rightMaterial);
//        rightCylinderXF.getChildren().add(rightbondCylinder);


        //add to body cylinder
        droneBodyXform.getChildren().add(droneBody); // add the drone
        droneBodyXform.getChildren().add(frontCylinderXF);
        droneBodyXform.getChildren().add(leftCylinderXF);
        droneBodyXform.getChildren().add(rightCylinderXF);

        //add sphere
        frontXform.getChildren().add(frontDroneSphere);
        ledtXform.getChildren().add(lefttDroneSphere);
        rightXform.getChildren().add(rightDroneSphere);

        droneComplex.getChildren().add(droneBodyXform);
        droneComplex.getChildren().add(frontXform);
        droneComplex.getChildren().add(ledtXform);
        droneComplex.getChildren().add(rightXform);


        frontXform.setTx(FRONT_DIST);
        ledtXform.setTx(LEFT_DIST);
        rightXform.setTx(RIGHT_DIST);
        frontCylinderXF.setTx(FRONT_DIST/2);
        leftCylinderXF.setTx(LEFT_DIST/2);
        rightCylinderXF.setTx(RIGHT_DIST/2);


        rightXform.setRotate(LEFT_ANGEL);//HYDROGEN_ANGLE 104.5
        ledtXform.setRotate(RIGHT_ANGEL);
        leftCylinderXF.setRotate(LEFT_ANGEL);
        rightCylinderXF.setRotate(RIGHT_ANGEL);

        root.getChildren().removeAll();
        droneGroup.getChildren().add(droneComplex);

        root.getChildren().addAll(droneGroup);
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX
     * application. main() serves only as fallback in case the
     * application can not be launched through deployment artifacts,
     * e.g., in IDEs with limited FX support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */

    public static void main(String[] args) {
        launch(args);
    }
}
